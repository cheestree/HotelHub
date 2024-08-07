<script lang="ts">
	import { Button, Input, Label, Select } from 'flowbite-svelte';
	import { register } from '../../hooks/auth';
	let username: string;
	let password: string;
	let email: string;
	let role: string;

	let roles = [
		{value: 'ADMIN', name: 'Admin'},
		{value: 'OWNER', name: 'Owner'},
		{value: 'EMPLOYEE', name: 'Employee'},
		{value: 'USER', name: 'User'}
	]

	async function handleRegister(username: string, password: string, email: string, role: string) {
		await register(username, password, email, role)
	}

</script>

<div class="flex h-fit shrink-0 items-center justify-center p-8 m-4 rounded-xl align-middle bg-slate-800">
	<form on:submit|preventDefault={() => handleRegister(username, password, email, role)} method="post">
		<div class="mb-6">
			<Label for="username" class="mb-2 block text-slate-400">Username</Label>
			<Input id="username" placeholder="JohnDoe" bind:value={username}></Input>
		</div>
		<div class="mb-6">
			<Label for="password" class="mb-2 block text-slate-400">Password</Label>
			<Input type="password" id="password" placeholder="Password123" bind:value={password}></Input>
		</div>
		<div class="mb-6">
			<Label for="email" class="mb-2 block text-slate-400">Email</Label>
			<Input type="email" id="email" placeholder="johndoe@gmail.com" bind:value={email}></Input>
		</div>
		<div class="mb-6">
			<Label for="email" class="mb-2 block text-slate-400">Password</Label>
			<Select id="role" items={roles} bind:value={role}></Select>
		</div>
		<Button type="submit">Register</Button>
	</form>
</div>
