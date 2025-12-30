<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow p-4" style="width: 100%; max-width: 400px;">
      <h3 class="text-center mb-4">Chinook Login</h3>

      <form @submit.prevent="handleLogin">
        <div class="mb-3">
          <label class="form-label">Email</label>
          <input
              type="email"
              class="form-control"
              v-model="email"
              placeholder="es. andrew@chinookcorp.com"
              required
          >
        </div>

        <div class="mb-3">
          <label class="form-label">Password</label>
          <input
              type="password"
              class="form-control"
              v-model="password"
              required
          >
        </div>

        <div v-if="errorMessage" class="alert alert-danger p-2 text-center">
          {{ errorMessage }}
        </div>

        <button type="submit" class="btn btn-primary w-100">Accedi</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '../stores/auth';
import { useRouter } from 'vue-router';

// Reactive state for form inputs
const email = ref('');
const password = ref('');
const errorMessage = ref('');

const authStore = useAuthStore();
const router = useRouter();

// Handle Form Submission
const handleLogin = async () => {
  errorMessage.value = ''; // Reset previous errors

  // Attempt login via Store
  const result = await authStore.login(email.value, password.value);

  if (result.success) {
    // Security Check: Redirect based on password status
    if (result.requiresPasswordChange) {
      router.push('/change-password');
    } else {
      router.push('/');
    }
  } else {
    // Display error to user
    errorMessage.value = result.message || 'Invalid credentials or server error.';
  }
};
</script>