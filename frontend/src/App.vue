<template>
  <header>
    <nav v-if="authStore.isAuthenticated" class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
      <div class="container">

        <span v-if="authStore.passwordChangeRequired" class="navbar-brand text-secondary" style="cursor: not-allowed;">
           Chinook Security App (Locked)
        </span>
        <RouterLink v-else class="navbar-brand" to="/">
          Chinook Security App
        </RouterLink>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
          <div class="navbar-nav ms-auto align-items-center">

            <span class="navbar-text me-3 text-light d-none d-md-block">
              Role: <strong>{{ authStore.userRole }}</strong>
            </span>

            <RouterLink
                v-if="!authStore.passwordChangeRequired"
                to="/change-password"
                class="btn btn-outline-light btn-sm me-2"
            >
              Change Password
            </RouterLink>

            <button @click="handleLogout" class="btn btn-outline-danger btn-sm">
              Logout
            </button>
          </div>
        </div>
      </div>
    </nav>
  </header>

  <RouterView />
</template>

<script setup>
import {onMounted, onUnmounted} from 'vue';
import {RouterView, RouterLink, useRouter} from 'vue-router';
import {useAuthStore} from './stores/auth';

const authStore = useAuthStore();
const router = useRouter();
let inactivityTimer = null;

const handleLogout = () => {
  authStore.logout();
  router.push('/login');
};

/*
 * INACTIVITY TIMER LOGIC
 * ----------------------
 * Automatically logs the user out after a specific period of inactivity.
 * Resets the timer on user interaction (mouse move, click, keypress).
 */

const resetTimer = () => {
  // Clear existing timer
  if (inactivityTimer) clearTimeout(inactivityTimer);

  // If authenticated, start a new 2-minute timer (120,000 ms)
  if (authStore.isAuthenticated) {
    inactivityTimer = setTimeout(() => {
      handleLogout();
      alert("Session expired due to inactivity.");
    }, 2 * 60 * 1000);
  }
};

// Setup Event Listeners on Mount
onMounted(() => {
  window.addEventListener('mousemove', resetTimer);
  window.addEventListener('click', resetTimer);
  window.addEventListener('keypress', resetTimer);

  // Initialize timer
  resetTimer();
});

// Cleanup Event Listeners on Unmount (Prevent Memory Leaks)
onUnmounted(() => {
  window.removeEventListener('mousemove', resetTimer);
  window.removeEventListener('click', resetTimer);
  window.removeEventListener('keypress', resetTimer);
  if (inactivityTimer) clearTimeout(inactivityTimer);
});
</script>

<style>
body {
  background-color: #f8f9fa;
}
</style>