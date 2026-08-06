// This script runs on all pages to handle the session UI in the Navbar
document.addEventListener('DOMContentLoaded', async () => {
    const navLinks = document.querySelector('.nav-links');
    if (!navLinks) return;

    try {
        const response = await fetch('/property-rental-app/api/auth/me');
        const session = await response.json();

        if (session.loggedIn) {
            let linksHtml = ``;
            if (session.role === 'Admin') {
                linksHtml += `<a href="AdminDashboard.html" style="color: var(--success-color);">Admin Dashboard</a>`;
            } else if (session.role === 'Host') {
                linksHtml += `<a href="AddProperty.html">Host your home</a>`;
            }
            linksHtml += `
                <span style="color: var(--text-secondary); margin-left: 2rem;">Welcome, ${session.name}</span>
                <a href="#" id="logoutBtn" style="color: var(--error-color);">Logout</a>
            `;
            navLinks.innerHTML = linksHtml;

            document.getElementById('logoutBtn').addEventListener('click', async (e) => {
                e.preventDefault();
                await fetch('/property-rental-app/api/auth/logout');
                window.location.href = 'index.html';
            });
        } else {
            navLinks.innerHTML = `
                <a href="Login.html">Log In</a>
                <a href="Register.html" style="background: var(--accent-color); padding: 0.5rem 1rem; border-radius: 0.5rem; color: white;">Sign Up</a>
            `;
        }
    } catch (e) {
        console.error("Auth check failed");
    }
});
