<template>
  <main class="container">

    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Lista Clienti</h1>
      <span class="badge bg-primary fs-6">{{ customers.length }} Clienti trovati</span>
    </div>

    <div class="card p-3 mb-4 shadow-sm">
      <form @submit.prevent="searchCustomers" class="row g-2">
        <div class="col-md-10">
          <input
              type="text"
              class="form-control"
              v-model="cityFilter"
              placeholder="Cerca per Città (es. Paris, Berlin...)"
          >
        </div>
        <div class="col-md-2">
          <button type="submit" class="btn btn-success w-100">Cerca</button>
        </div>
      </form>
    </div>

    <div v-if="loading" class="text-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Caricamento...</span>
      </div>
    </div>

    <div v-else class="table-responsive card shadow-sm">
      <table class="table table-striped table-hover mb-0">
        <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Cognome</th>
          <th>Azienda</th>
          <th>Città</th>
          <th>Email</th>
          <th>Support Rep</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="customer in customers" :key="customer.customerId">
          <td>{{ customer.customerId }}</td>
          <td>{{ customer.firstName }}</td>
          <td><strong>{{ customer.lastName }}</strong></td>
          <td>{{ customer.company || '-' }}</td>
          <td>{{ customer.city }}</td>
          <td>
            <a :href="'mailto:' + customer.email">{{ customer.email }}</a>
          </td>
          <td>
              <span class="badge bg-secondary">
                {{ customer.supportRep ? customer.supportRep.firstName : 'N/A' }}
              </span>
          </td>
        </tr>

        <tr v-if="customers.length === 0">
          <td colspan="7" class="text-center py-3 text-muted">
            Nessun cliente trovato.
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';
import { useAuthStore } from '../stores/auth';

const customers = ref([]);
const loading = ref(false);
const cityFilter = ref('');
const authStore = useAuthStore();

/**
 * Fetches the customer list from the Backend.
 * Handles both the full list and city-filtered queries.
 */
const fetchCustomers = async () => {
  loading.value = true;
  try {
    // Build query URL based on filter presence
    const url = cityFilter.value
        ? `/customers?city=${cityFilter.value}`
        : '/customers';

    const response = await api.get(url);
    customers.value = response.data;
  } catch (error) {
    alert("Error loading data. Are you logged in?");
  } finally {
    loading.value = false;
  }
};

// Trigger search
const searchCustomers = () => {
  fetchCustomers();
};

// Lifecycle Hook: Load data when component is mounted
onMounted(() => {
  fetchCustomers();
});
</script>