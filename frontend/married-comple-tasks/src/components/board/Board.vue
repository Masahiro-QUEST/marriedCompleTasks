<template>
  <div class="board">
    <h1>夫婦の未来</h1>
    <Modal :visible="isAddTaskModalVisible" @close="isAddTaskModalVisible = false">
      <div>
        <h2>新しいタスクを追加</h2>
        <form @submit.prevent="addTask">
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
import axios from 'axios'
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
          cards: [{}],
        },
        {
          id: 2,
          title: '進行中',
          cards: [{}],
        },
        {
          id: 3,
          title: '完了',
          cards: [{}],
        },
      ],
      newTask: { titleLabel: '', dateLabel: '', description: '' },
      selectedColumnId: null,
      isCardModalVisible: false,
      isAddTaskModalVisible: false,
    }
  },
  methods: {
    async fetchTasks() {
      try {
        const response = await axios.get('http://localhost:8080/api/tasks')
        const tasks = response.data
        console.log('Fetched tasks:', tasks)
        // タスクデータを `cards` に変換
        const cards = tasks.map((task) => ({
          id: task.id,
          titleLabel: task.title,
          dateLabel: task.registerDate ? this.formatDate(task.registerDate) : '',
          description: task.description,
          completed: task.completed,
        }))

        // Vue の reactivity に合わせてリストを更新
        this.lists = [
          { id: 1, title: '未着手', cards: cards.filter((task) => !task.completed) },
          { id: 2, title: '進行中', cards: [] }, // 進行中のタスクをどうするかによる
          { id: 3, title: '完了', cards: cards.filter((task) => task.completed) },
        ]
      } catch (error) {
        console.error('Failed to fetch tasks:', error)
      }
    },
    formatDate(dateString) {
      if (!dateString) return ''
      return `${dateString.slice(0, 4)}-${dateString.slice(4, 6)}-${dateString.slice(6, 8)}`
    },
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
    async addTask() {
      try {
        // サーバーに送るデータの型を合わせる
        const requestData = {
          title: this.newTask.titleLabel,
          registerDate: this.newTask.dateLabel.replaceAll('-', ''),
          description: this.newTask.description,
          status: '未完了',
        }
        // 実際にPOSTリクエストを投げる
        const response = await axios.post('http://localhost:8080/api/tasks', requestData)

        // バックエンドからのレスポンスに新規タスクのIDなどが含まれている場合はこちら
        const createdTask = response.data
      } catch (error) {
        console.error('Failed to add task:', error)
      }
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
  async mounted() {
    await this.fetchTasks()
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
