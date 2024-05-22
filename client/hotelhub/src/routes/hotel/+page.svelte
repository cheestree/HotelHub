<script lang="ts">
	import type { HotelModel } from './Type';
	import { onMount } from 'svelte';
	import Hotel from './hotel.svelte';

	const endpoint = "/api/hotel";
	let hotels: HotelModel[] = [];

	onMount(async function () {
		const response = await fetch(endpoint);
		hotels = await response.json();
	});
</script>

<div class="grid grid-cols-3">
	{#each hotels as hotel}
		<Hotel hotel={hotel}></Hotel>
	{/each}
</div>
