import { ref, computed } from "vue";
import { useApi } from "./useApi";
import { useAuthStore } from "~/stores/auth";
import { apiConfig } from "~/config/api.config";

interface User {
  id: number;
  email: string;
  role: string;
  username: string;
}

// Cookie Helper Funktionen
const setCookie = (name: string, value: string, days = 7) => {
  const expires = new Date(Date.now() + days * 864e5).toUTCString();
  document.cookie = `${name}=${encodeURIComponent(
    value
  )}; expires=${expires}; path=/; Secure; SameSite=Strict`;
};

const getCookie = (name: string): string | null => {
  const cookie = document.cookie
    .split("; ")
    .find((row) => row.startsWith(`${name}=`));
  return cookie ? decodeURIComponent(cookie.split("=")[1]) : null;
};

const deleteCookie = (name: string) => {
  document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
};

export function useAuth() {
  const authStore = useAuthStore();
  const environment = process.env.NODE_ENV || "development";

  const config = apiConfig[environment as keyof typeof apiConfig];

  const getUserUrl = () => config.userUrl;

  const getAuthHeader = (username: string, password: string) => {
    return { Authorization: `Basic ${btoa(`${username}:${password}`)}` };
  };

  const register = async (
    firstName: string,
    lastName: string,
    username: string,
    password: string
  ) => {
    try {
      const response = await fetch(`${getUserUrl()}/users`, {
        method: "POST",
        headers: {
          ...getAuthHeader("admin", "master"),
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          firstName: firstName,
          lastName: lastName,
          username: username,
          password: password,
          userRole: "USER",
        }),
      });

      if (!response.ok) {
        throw new Error("Registration failed");
      }

      const data = await response.json();
      return { data };
    } catch (error) {
      console.error("Registration error:", error);
      throw error;
    }
  };

  const login = async (username: string, password: string) => {
    try {
      if (!username || !password) {
        throw new Error("Username and password are required");
      }

      const response = await fetch(`${getUserUrl()}/users/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${btoa("admin:master")}`,
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (response.status === 200) {
        const data = await response.json();
        // userId aus Response speichern
        authStore.setAuth(username, password, data.userId);
        return { user: authStore.user, ...data };
      }

      if (response.status === 401) {
        throw new Error("Invalid credentials");
      }

      throw new Error("Login failed");
    } catch (error) {
      console.error("Login error:", error);
      throw error;
    }
  };

  const logout = () => {
    authStore.clearAuth();
  };

  const getUser = async () => {
    try {
      if (!process.client || !authStore.credentials || !authStore.user?.userId)
        return null;

      // Nutze den vorhandenen Endpunkt /users/{id}
      const response = await fetch(
        `${getUserUrl()}/users/${authStore.user.userId}`,
        {
          headers: { Authorization: `Basic ${authStore.credentials}` },
        }
      );

      if (!response.ok) {
        console.error("User validation failed");
        logout();
        return null;
      }

      // Optional: User-Daten aktualisieren
      const userData = await response.json();
      if (userData) {
        // Bestehende userId erhalten und mit neuen Daten aktualisieren
        authStore.updateUser(userData);
      }

      return authStore.user;
    } catch (error) {
      console.error("Error loading user:", error);
      logout();
      return null;
    }
  };

  return {
    user: computed(() => authStore.user),
    register,
    login,
    getUser,
    logout,
    getAuthHeader,
  };
}
