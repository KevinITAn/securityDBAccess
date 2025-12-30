import { defineStore } from 'pinia';
import api from '../services/api';

export const useAuthStore = defineStore('auth', {
    // ----------------
    // 1. STATE
    // ----------------
    // Initialize state from LocalStorage to persist data across page reloads
    state: () => ({
        token: localStorage.getItem('token') || null,
        userRole: localStorage.getItem('userRole') || null,
        // Convert string 'true'/'false' from storage to boolean
        passwordChangeRequired: localStorage.getItem('passwordChangeRequired') === 'true',
    }),

    // ----------------
    // 2. GETTERS
    // ----------------
    getters: {
        isAuthenticated: (state) => !!state.token,
        isManager: (state) => state.userRole === 'MANAGER',
    },

    // ----------------
    // 3. ACTIONS
    // ----------------
    actions: {

        /**
         * Authenticates the user against the backend.
         * Updates the state and LocalStorage upon success.
         */
        async login(email, password) {
            try {
                const response = await api.post('/auth/login', { email, password });

                // Update Pinia State
                this.token = response.data.token;
                this.userRole = response.data.role;
                this.passwordChangeRequired = response.data.requiresPasswordChange;

                // Persist to LocalStorage for session continuity
                localStorage.setItem('token', this.token);
                localStorage.setItem('userRole', this.userRole);
                localStorage.setItem('passwordChangeRequired', this.passwordChangeRequired);

                return {
                    success: true,
                    requiresPasswordChange: this.passwordChangeRequired
                };

            } catch (error) {
                return { success: false, message: "Invalid credentials or server error." };
            }
        },

        /**
         * Handles the password update process.
         * Unlocks the user session if the update is successful.
         */
        async changePassword(oldPassword, newPassword) {
            try {
                await api.put('/employees/password', {
                    oldPassword,
                    newPassword
                });

                // Unlock the user session (Security Lock removed)
                this.passwordChangeRequired = false;
                localStorage.setItem('passwordChangeRequired', 'false');

                return { success: true, message: "Password updated successfully." };
            } catch (error) {
                const msg = error.response && error.response.data
                    ? error.response.data
                    : "Error updating password.";

                return { success: false, message: msg };
            }
        },

        /**
         * Clears all authentication data from State and LocalStorage.
         */
        logout() {
            // Reset State
            this.token = null;
            this.userRole = null;
            this.passwordChangeRequired = false;

            // Clear Storage
            localStorage.removeItem('token');
            localStorage.removeItem('userRole');
            localStorage.removeItem('passwordChangeRequired');
        }
    },
});