<template>
  <div class="board">
    <h1>夫婦の未来</h1>
    <Modal :visible="isAddTaskModalVisible" @close="isAddTaskModalVisible = false">
      <div>
        <h2>新しいタスクを追加</h2>
        <form @submit="handleSubmit" @submit.prevent="addTask">
          <label>
            タイトル:
            <input v-model="newTask.titleLabel" required />
          </label>
          <label>
            日付:
            <input type="date" v-model="newTask.dateLabel" required />
          </label>
          <label>
            説明:
            <textarea v-model="newTask.description" required></textarea>
          </label>
          <button type="submit">追加</button>
        </form>
      </div>
    </Modal>
    <div>
      <button @click="openAddTaskModal">追加</button>
    </div>
    <div class="lists">
      <Column
        v-for="list in lists"
        :key="list.id"
        :column="list"
        @add-card="handleAddCard"
        @delete-card="handleDeleteCard"
        @card-clicked="showCardModal"
        @update-lists="updateLists"
      />
      <Modal :visible="isCardModalVisible" @close="isCardModalVisible = false">
        <div>
          <h2>{{ selectedCard.titleLabel }}</h2>
          <p>{{ selectedCard.dateLabel }}</p>
          <p>{{ selectedCard.description }}</p>
        </div>
      </Modal>
    </div>
  </div>
</template>

<script>
import Column from '../column/Column.vue'
import Modal from '../modal/Modal.vue'
export default {
  name: 'Board',
  components: { Column, Modal },
  data() {
    return {
      lists: [
        {
          id: 1,
          title: '未着手',
          cards: [
            {
              id: 1,
              titleLabel: 'タスクタイトル1',
              dateLabel: '2021-01-01',
              description: 'Description for Task 1',
            },
            {
              id: 2,
              titleLabel: 'タスクタイトル2',
              dateLabel: '2021-01-02',
              description: 'Description for Task 2',
            },
          ],
        },
        {
          id: 2,
          title: '計画中',
          cards: [
            {
              id: 3,
              titleLabel: 'タスクタイトル3',
              dateLabel: '2021-01-02',
              description: 'Description for Task 3',
            },
          ],
        },
        {
          id: 3,
          title: '進行中',
          cards: [
            {
              id: 4,
              titleLabel: 'タスクタイトル4',
              dateLabel: '2021-01-02',
              description: 'Description for Task 4',
            },
          ],
        },
        {
          id: 4,
          title: '完了',
          cards: [
            {
              id: 5,
              titleLabel: 'タスクタイトル5',
              dateLabel: '2021-01-02',
              description: 'Description for Task 5',
            },
          ],
        },
      ],
      newTask: { titleLabel: '', dateLabel: '', description: '' },
      selectedColumnId: null,
      isCardModalVisible: false,
      isAddTaskModalVisible: false,
    }
  },
  methods: {
    handleAddCard(columnId) {
      // カード追加のロジックをここに実装
      this.isCardModalVisible = true
    },
    handleDeleteCard(columnId, cardId) {
      // カード削除のロジックをここに実装
    },
    showCardModal({ columnId, cardId }) {
      // カードIDを基に選択されたカードを特定
      const column = this.lists.find((list) => list.id === columnId)
      const card = column.cards.find((card) => card.id === cardId)

      this.selectedCard = card
      this.isCardModalVisible = true
    },
    updateLists() {
      // リスト更新時に必要な処理があればここで追加
      console.log('Lists updated:', this.lists)
    },
    openAddTaskModal(coloumnId) {
      this.selectedColumnId = coloumnId
      this.isAddTaskModalVisible = true
    },
    addTask() {
      // タスク追加のロジックをここに実装
      const column = this.lists.find((list) => list.id === this.selectedColumnId)
      column.cards.push({
        id: column.cards.length + 1,
        titleLabel: this.newTask.titleLabel,
        dateLabel: this.newTask.dateLabel,
        description: this.newTask.description,
      })
      this.isAddTaskModalVisible = false
    },
    updateLists() {
      // リスト更新時に必要な処理があればここで追加
      console.log('Lists updated:', this.lists)
    },
    handleSubmit() {
      console.log('Form submitted')
      isAddTaskModalVisible = false
    },
  },
}
</script>

<style scoped>
.board {
  padding: 16px;
  background-color: #f4f4f4;
}

h1 {
  text-align: center;
  margin-bottom: 16px;
  color: #333;
}

.lists {
  display: flex; /* 横並びにする */
  gap: 16px; /* 各カラム間のスペースを調整 */
  overflow-x: auto; /* 横スクロールを許可 */
  padding: 8px;
}

.lists::-webkit-scrollbar {
  height: 8px; /* 横スクロールバーの高さ */
}

.lists::-webkit-scrollbar-thumb {
  background-color: #bbb; /* スクロールバーの色 */
  border-radius: 4px; /* スクロールバーの角丸 */
}

.lists::-webkit-scrollbar-track {
  background-color: #f0f0f0; /* スクロールバーの背景色 */
}
</style>
