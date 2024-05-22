<script lang="ts">
	import { Button } from 'flowbite-svelte';

	let ratings: number = 0; // Array to store ratings for each set
	let selected: boolean = false;

	function handleHover(event: MouseEvent, value: number) {
		if(!selected) ratings = value;
	}

	function handleLeave(){
		if(!selected) ratings = 0;
	}

	function handleClick(event: MouseEvent, value: number) {
		selected = true;
		ratings = value;
	}

	function handleFocus(value: number) {
		if(!selected) ratings = value;
	}
</script>

<div class="rating">
	{#each [1, 2, 3, 4, 5] as value}
    <Button
			class="star bg-transparent"
			role="button"
			on:mouseenter={e => handleHover(e, value)}
			on:mouseleave={handleLeave}
			on:click={e => handleClick(e, value)}
			on:focus={() => handleFocus(value)}
			tabindex="0"
			data-value={value}
			style="color: {ratings >= value ? 'yellow' : 'gray'}"
		>
      &#9733;
    </Button>
	{/each}
</div>
