<script>
	import { Navbar, NavBrand, NavLi, NavUl, NavHamburger, Button, Search } from 'flowbite-svelte';
</script>

<div class="relative px-8">
	<Navbar class="fixed start-0 top-0 z-20 w-full border-b px-2 py-2.5 sm:px-4">
		<NavBrand href="/">
			<span class="self-center whitespace-nowrap text-xl font-semibold dark:text-white"
				>Flowbite</span
			>
		</NavBrand>
		<NavHamburger />
		<NavUl>
			<NavLi href="/" active={true}>Home</NavLi>
			<NavLi href="/search">Search</NavLi>
			<NavLi href="/hotel">Hotels</NavLi>
			<NavLi href="/support">Support</NavLi>
			<NavLi href="/about">About</NavLi>
			<NavLi href="/login">Login</NavLi>
			<NavLi href="/register">Register</NavLi>
		</NavUl>
		<form class="flex gap-2">
			<Search size="md" />
			<Button class="!p-2.5">
				<svg
					class="h-6 w-6 text-gray-800 dark:text-white"
					aria-hidden="true"
					xmlns="http://www.w3.org/2000/svg"
					fill="none"
					viewBox="0 0 24 24"
				>
					<path
						stroke="currentColor"
						stroke-linecap="round"
						stroke-width="2"
						d="m21 21-3.5-3.5M17 10a7 7 0 1 1-14 0 7 7 0 0 1 14 0Z"
					/>
				</svg>
			</Button>
		</form>
	</Navbar>
</div>
