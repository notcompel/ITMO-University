<template>
    <footer>

        Posts: {{ postsL }}
        Users: {{ userL }}
        <a href="/">Codeforces</a> &copy; 2022 by Mike Mirzayanov
    </footer>
</template>

<script>
export default {
    name: "Footer",
    props: ["postsL", "userL"]
}
</script>

<style scoped>

</style>