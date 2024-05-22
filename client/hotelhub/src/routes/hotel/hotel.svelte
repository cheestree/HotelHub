<script lang="ts">
	import type { HotelModel } from './Type.js';
	import { goto } from '$app/navigation';
	import { Button } from 'flowbite-svelte';
	export let hotel: HotelModel;

	function handleClick() {
		goto("/api/hotel/"+hotel.id)
	}
</script>

<style>
    .hotel-property {
        font-weight: bold;
        margin-bottom: 8px;
    }
    .hotel-name:hover {
        text-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    }
</style>

<div class="border border-gray-300 rounded-lg p-4 mb-4">
	<button class="hotel-property" on:click={() => { handleClick() }}> <span class="hotel-name hover:text-transparent bg-clip-text bg-gray-200">Name: {hotel.name}</span></button>
	<div class="hotel-property">Address: {hotel.address}</div>
	<div class="hotel-property">Stars: {hotel.stars}</div>
	<div class="hotel-property">Latitude: {hotel.latitude}</div>
	<div class="hotel-property">Longitude: {hotel.longitude}</div>
</div>
