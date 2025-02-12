<template>
  <div class="card" @click="handleClick">
    <div class="card-header">
      <div class="card-title">{{ titleLabel }}</div>
    </div>
    <div class="card-body"></div>
    <div class="card-footer"></div>
    <div class="card-title">{{ dateLabel }}</div>
  </div>
</template>

<script>
export default {
  name: 'Card',
  props: {
    titleLabel: {
      type: String,
      required: true,
    },
    dateLabel: {
      type: String,
      required: true,
    },
    card: {
      type: Object,
      required: true,
    },
  },
  methods: {
    handleClick() {
      this.$emit('card-clicked', this.card.id)
    },
  },
}
</script>

<style scoped>
.card {
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 16px;
  margin: 8px;
  background-color: #fff;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-body {
  margin-top: 8px;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}

.card-title {
  margin: 0;
  color: #000;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
