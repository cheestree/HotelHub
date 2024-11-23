<script lang="ts">
	import '../app.css';
	let { children } = $props();
  </script>
  
  <div class="h-full flex flex-col bg-black">
	<header class="bg-blue-500 p-4">
	  <nav>
		<!-- Your navigation links -->
		<a href="/">Home</a>
		<a href="/about">About</a>
	  </nav>
	</header>
  
	<main class="flex-1">
	  {@render children()}
	</main>
  
	<footer class="bg-gray-800 text-white p-4">
	  <!-- Your footer content -->
	  <p>© 2024 My App</p>
	</footer>
  </div>