import { createClient } from '@supabase/supabase-js';

// const supabaseUrl = useRuntimeConfig().public.SUPABASE_URL;
// const supabaseAnonKey = useRuntimeConfig().public.SUPABASE_ANON_KEY;

export const useSupabase = () => {
    const config = useRuntimeConfig()
    return createClient(config.public.supabaseUrl, config.public.supabaseAnonKey);
}