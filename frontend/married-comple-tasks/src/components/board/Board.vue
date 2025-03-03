<template>
  <div class="board">
    <h1>夫婦の未来</h1>
    <!-- タスク追加用モーダル -->
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

      <!-- カード詳細用モーダル -->
      <Modal
        :visible="isCardModalVisible"
        :taskId="selectedCard?.id"
        @close="closeCardModal"
        @deleted="handleDeleteCard"
      >
        <div v-if="selectedCard">
          <h2>{{ selectedCard.titleLabel }}</h2>
          <p>{{ selectedCard.dateLabel }}</p>
          <p>{{ selectedCard.description }}</p>
          <div class="editClass">
            <!-- 編集ボタン押下で編集用モーダルに切替 -->
            <div @click="openEditTaskModal" class="edit-button">編集</div>
            <div @click="handleDeleteCard(selectedCard.id)" class="delete-button">削除</div>
          </div>
        </div>
      </Modal>
    </div>

    <!-- 編集用モーダル -->
    <Modal :visible="isEditTaskModalVisible" @close="isEditTaskModalVisible = false">
      <div v-if="selectedCard">
        <h2>タスク編集</h2>
        <form @submit.prevent="updateTask">
          <label>
            タイトル:
            <input v-model="editTaskData.titleLabel" required />
          </label>
          <label>
            日付:
            <input type="date" v-model="editTaskData.dateLabel" required />
          </label>
          <label>
            説明:
            <textarea v-model="editTaskData.description" required></textarea>
          </label>
          <button type="submit">更新</button>
        </form>
      </div>
    </Modal>
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
        { id: 1, title: '未着手', cards: [] },
        { id: 2, title: '進行中', cards: [] },
        { id: 3, title: '完了', cards: [] },
      ],
      newTask: { titleLabel: '', dateLabel: '', description: '' },
      selectedCard: null,
      isCardModalVisible: false,
      isAddTaskModalVisible: false,
      isEditTaskModalVisible: false,
      editTaskData: {},
    }
  },
  methods: {
    async fetchTasks() {
      try {
        const response = await axios.get('http://localhost:8080/api/tasks')
        const tasks = response.data
        console.log('Fetched tasks:', tasks)
        // タスクデータを cards 用に変換
        const cards = tasks.map((task) => ({
          id: task.id,
          titleLabel: task.title,
          dateLabel: task.registerDate ? this.formatDate(task.registerDate) : '',
          description: task.description,
          completed: task.completed,
        }))
        // リスト毎にカードを振り分け
        this.lists = [
          { id: 1, title: '未着手', cards: cards.filter((task) => !task.completed) },
          { id: 2, title: '進行中', cards: [] },
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
      this.isCardModalVisible = true
    },
    handleDeleteCard(deletedTaskId) {
      this.lists.forEach((list) => {
        list.cards = list.cards.filter((card) => card.id !== deletedTaskId)
      })
      this.closeCardModal()
    },
    showCardModal({ columnId, cardId }) {
      const column = this.lists.find((list) => list.id === columnId)
      if (!column) return
      const card = column.cards.find((card) => card.id === cardId)
      if (!card) return
      this.selectedCard = card
      this.isCardModalVisible = true
    },
    openAddTaskModal() {
      this.isAddTaskModalVisible = true
    },
    async addTask() {
      try {
        const requestData = {
          title: this.newTask.titleLabel,
          registerDate: this.newTask.dateLabel.replaceAll('-', ''),
          description: this.newTask.description,
          status: '未完了',
        }
        await axios.post('http://localhost:8080/api/tasks', requestData)
        await this.fetchTasks()
      } catch (error) {
        console.error('Failed to add task:', error)
      }
      this.isAddTaskModalVisible = false
    },
    updateLists() {
      console.log('Lists updated:', this.lists)
    },
    openEditTaskModal() {
      if (this.selectedCard) {
        // 現在選択中のタスクの内容を編集用データにコピー
        this.editTaskData = { ...this.selectedCard }
        // 編集用モーダルを表示し、カード詳細モーダルは閉じる
        this.isEditTaskModalVisible = true
        this.isCardModalVisible = false
      }
    },
    async updateTask() {
      if (!this.selectedCard) return
      try {
        const requestData = {
          title: this.editTaskData.titleLabel,
          registerDate: this.editTaskData.dateLabel.replaceAll('-', ''),
          description: this.editTaskData.description,
          status: '未完了',
        }
        await axios.put(`http://localhost:8080/api/tasks/${this.selectedCard.id}`, requestData)
        await this.fetchTasks()
      } catch (error) {
        console.error('Failed to update task:', error)
      }
      this.isEditTaskModalVisible = false
    },
    closeCardModal() {
      this.isCardModalVisible = false
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
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding: 8px;
}
.lists::-webkit-scrollbar {
  height: 8px;
}
.lists::-webkit-scrollbar-thumb {
  background-color: #bbb;
  border-radius: 4px;
}
.lists::-webkit-scrollbar-track {
  background-color: #f0f0f0;
}
.editClass {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}
.edit-button {
  cursor: pointer;
  color: blue;
}
.delete-button {
  cursor: pointer;
  color: red;
}
</style>
