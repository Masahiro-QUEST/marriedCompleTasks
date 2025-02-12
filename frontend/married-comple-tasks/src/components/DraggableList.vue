<template>
  <div class="container">
    <h2>Drag and Drop Example</h2>
    <draggable v-model="items" class="list" @end="onDragEnd">
      <div v-for="item in items" :key="item.id" class="list-item">
        {{ item.name }}
      </div>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: 'DraggableList',
  components: { draggable },
  data() {
    return {
      items: [
        { id: 1, name: 'Item 1' },
        { id: 2, name: 'Item 2' },
        { id: 3, name: 'Item 3' },
      ],
    }
  },
  methods: {
    onDragEnd() {
      console.log('Drag ended. New order:', this.items)
    },
  },
}
</script>

<style scoped>
.container {
  max-width: 400px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f9f9f9;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.list-item {
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: grab;
}

.list-item:active {
  cursor: grabbing;
}
</style>
