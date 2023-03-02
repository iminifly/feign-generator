<template>
    <div class="source-dialog-div">
        <pre>{{ source }}</pre>
    </div>
</template>
<script>
export default {
    name: 'SourceDialog',
    props: {
        source: {
            type: String,
            required: true
        }
    },
    data() {
        return {}
    },
    methods: {
        close() {
            this.$emit('func', false)
        },
        success() {
            this.$emit('success', false)
        }
    }
}
</script>
