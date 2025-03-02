<template>
  <div class="modal" v-if="visible">
    <div class="modal-overlay" @click="close"></div>
    <div class="modal-content" @click.stop>
      <button class="close-button" @click="close">×</button>
      <slot></slot>
      <!-- taskIdがある場合のみ削除ボタンを表示 -->
      <div v-if="taskId" class="editClass">
        <div>編集</div>
        <div @click="handleDelete" class="delete-button" :class="{ loading: isDeleting }">
          {{ isDeleting ? '削除中...' : '削除' }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'Modal',
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
    taskId: {
      type: Number,
      default: null,
    },
  },
  data() {
    return {
      isDeleting: false,
    }
  },
  methods: {
    close() {
      this.$emit('close')
    },
    async handleDelete() {
      if (!this.taskId) return
      this.isDeleting = true
      try {
        await axios.delete(`http://localhost:8080/api/tasks/${this.taskId}`)
        // 削除が成功したら、親コンポーネントに削除完了イベントを通知
        this.$emit('deleted', this.taskId)
        this.close()
      } catch (error) {
        console.error('削除に失敗しました', error)
      } finally {
        this.isDeleting = false
      }
    },
  },
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  position: relative;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5em;
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
}

.editClass {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.delete-button {
  cursor: pointer;
  color: red;
}

.delete-button.loading {
  color: gray;
  pointer-events: none;
}
</style>
