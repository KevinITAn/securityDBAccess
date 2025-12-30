import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from '../views/HomeView.vue'
import ChangePasswordView from '../views/ChangePasswordView.vue'

// Route Definitions
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/',
            name: 'home',
            component: HomeView,
            meta: { requiresAuth: true } // Protected Route
        },
        {
            path: '/change-password',
            name: 'change-password',
            component: ChangePasswordView,
            meta: { requiresAuth: true } // Protected Route
        }
    ]
})

/*
 * Global Navigation Guard
 * -----------------------
 * Intercepts every route change to enforce security policies:
 * 1. Authentication: Redirects unauthenticated users to Login.
 * 2. Security Lock: Forces users with default passwords to the 'change-password' page.
 */
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    // Check if the user is flagged to change their password
    const passwordChangeRequired = localStorage.getItem('passwordChangeRequired') === 'true';

    // 1. If the route requires auth but no token is found -> Redirect to Login
    if (to.meta.requiresAuth && !token) {
        next('/login');
    }
        // 2. If authenticated BUT password change is required -> Force redirect to Change Password
    //    (Unless they are already on the change-password page)
    else if (token && passwordChangeRequired && to.name !== 'change-password') {
        next('/change-password');
    }
    // 3. Allow navigation
    else {
        next();
    }
});

export default router