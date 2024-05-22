import { writable } from 'svelte/store';
import { goto } from '$app/navigation';
export const store = writable(null);
export const login = async (username: string, password: string) => {
	const req = await fetch('/api/user/token', {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		credentials: "include",
		body: JSON.stringify({"username": username, "password": password})
	})
	await goto(req.ok ? "/home" : "/login")
}

export const register = async (username: string, password: string, email: string) => {
	await fetch('/api/user', {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		credentials: "include",
		body: JSON.stringify({"username": username, "password": password, "email": email})
	})
}
