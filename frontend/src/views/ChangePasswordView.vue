<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card shadow">
          <div class="card-header bg-primary text-white">
            <h4 class="mb-0">Cambia Password</h4>
          </div>
          <div class="card-body">

            <form @submit.prevent="handleChange">

              <div class="mb-3">
                <label class="form-label">Vecchia Password</label>
                <input type="password" class="form-control" v-model="oldPassword" required>
              </div>

              <div class="mb-3">
                <label class="form-label">Nuova Password</label>
                <input type="password" class="form-control" v-model="newPassword" required>
                <div class="form-text">
                  Deve contenere: 1 Maiuscola, 1 Minuscola, 1 Numero, 1 Speciale (-!$#%).
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label">Conferma Nuova Password</label>
                <input type="password" class="form-control" v-model="confirmPassword" required>
              </div>

              <div v-if="message" :class="['alert', isError ? 'alert-danger' : 'alert-success']">
                {{ message }}
              </div>

              <div class="d-flex justify-content-between">
                <button type="button" class="btn btn-secondary" @click="$router.push('/')">Annulla</button>
                <button type="submit" class="btn btn-primary">Salva Password</button>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {useAuthStore} from '../stores/auth';

// Component State
const oldPassword = ref('');
const newPassword = ref('');
const confirmPassword = ref('');
const message = ref('');
const isError = ref(false);

const authStore = useAuthStore();

/**
 * Validates inputs and submits the password change request.
 */
const handleChange = async () => {
  message.value = '';
  isError.value = false;

  // 1. Validation: Passwords must match
  if (newPassword.value !== confirmPassword.value) {
    isError.value = true;
    message.value = "Le nuove password non coincidono!";
    return;
  }

  // 2. Validation: Regex Complexity Check (Client-side)
  // Enforces: 1 Number, 1 Lower, 1 Upper, 1 Special Char, Length 6-14
  const regex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-!$#%]).{6,14}$/;
  if (!regex.test(newPassword.value)) {
    isError.value = true;
    message.value = "La password non rispetta i requisiti di complessità.";
    return;
  }

  // 3. API Call
  const result = await authStore.changePassword(oldPassword.value, newPassword.value);

  if (result.success) {
    isError.value = false;
    message.value = "Password aggiornata con successo!";
    // Clear form inputs
    oldPassword.value = '';
    newPassword.value = '';
    confirmPassword.value = '';
  } else {
    isError.value = true;
    message.value = result.message;
  }
};
</script>