<template>
  <div class="modal" v-if="visible">
    <!-- 背景部分のクリックでモーダルを閉じる -->
    <div class="modal-overlay" @click="close"></div>

    <!-- モーダル内部 -->
    <div class="modal-content" @click.stop>
      <!-- 閉じるボタン -->
      <button class="close-button" @click="close">×</button>

      <!-- スロット内容を表示 -->
      <slot></slot>

      <!-- 編集・削除セクション -->
      <div class="editClass">
        <div>編集</div>
        <div>削除</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Modal',
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    close() {
      this.$emit('close')
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
</style>
