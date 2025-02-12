<template>
  <div class="column">
    <h2 class="coloum-title">{{ column.title }}</h2>
    <div class="cards">
      <draggable v-model="column.cards" group="cards" item-key="id" @end="onDragEnd">
        <template #item="{ element }">
          <Card
            :card="element"
            :titleLabel="element.titleLabel"
            :dateLabel="element.dateLabel"
            @click="handleCardClick(element.id)"
          />
        </template>
      </draggable>
    </div>
  </div>
</template>

<script>
import Card from '../card/Card.vue'
import draggable from 'vuedraggable'

export default {
  name: 'Column',
  components: {
    Card,
    draggable,
  },
  props: {
    column: {
      type: Object,
      required: true,
    },
  },
  methods: {
    handleCardClick(cardId) {
      this.$emit('card-clicked', { columnId: this.column.id, cardId })
    },
    onDragEnd(event) {
      // カード移動完了時のイベント。必要に応じて追加処理を記述
      this.$emit('update-lists')
    },
  },
}
</script>

<style scoped>
/* 既存のスタイルをそのまま利用 */
.column {
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 16px;
  margin: 8px;
  background-color: #f9f9f9;
}

.coloum-title {
  margin: 0;
  color: #000;
}

.cards {
  margin-top: 16px;
}
</style>
