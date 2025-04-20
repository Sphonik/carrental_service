export default defineNuxtRouteMiddleware(async(to, from) => {
    const { getUser } = useAuth()
    const user = await getUser()

    if (!user) {
        return navigateTo('/login');
      }
      const allowedRoles = to.meta.roles;
      if (allowedRoles && !allowedRoles.includes(user.value.role)) {
        return navigateTo('/unauthorized');
      }
    });