import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { currencyConfig } from '~/config/currency.config'

const currencySymbols = currencyConfig.currencySymbols;

type CurrencySymbol = keyof typeof currencySymbols;

// Globaler reaktiver State außerhalb der Funktion
const globalCurrentCurrency = ref<CurrencySymbol>("USD");

export function useCurrency() {
  const route = useRoute();

  const setCurrentCurrency = (currency: string) => {
    if (currency in currencySymbols) {
      globalCurrentCurrency.value = currency as CurrencySymbol;
      localStorage.setItem("selectedCurrency", currency);
    }
  };

  const getCurrentSymbol = () => {
    return currencySymbols[globalCurrentCurrency.value] || "€";
  };

  // Initialisierung mit Route oder localStorage
  onMounted(() => {
    // Prüfe zuerst Route Query Parameter
    if (route.query.currency && typeof route.query.currency === "string") {
      setCurrentCurrency(route.query.currency);
    } else {
      // Sonst verwende localStorage
      const savedCurrency = localStorage.getItem("selectedCurrency");
      if (savedCurrency && savedCurrency in currencySymbols) {
        setCurrentCurrency(savedCurrency);
      }
    }
  });

  return {
    currentCurrency: globalCurrentCurrency,
    setCurrentCurrency,
    getCurrentSymbol,
    currencySymbols,
  };
}
