/**
 * 文件处理工具类
 */
export class FileUtils {
  /**
   * 验证图片文件
   * @param {File} file - 文件对象
   * @param {number} maxSize - 最大文件大小（KB）
   * @returns {Object} 验证结果 {isValid: boolean, message: string}
   */
  static validateImageFile(file, maxSize = 500) {
    // 检查文件类型
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png']
    if (!allowedTypes.includes(file.type)) {
      return {
        isValid: false,
        message: '只能上传jpg/png文件!'
      }
    }

    // 检查文件大小
    const fileSizeKB = file.size / 1024
    if (fileSizeKB > maxSize) {
      return {
        isValid: false,
        message: `文件大小不能超过${maxSize}kb!`
      }
    }

    return {
      isValid: true,
      message: '文件验证通过'
    }
  }

  /**
   * 生成文件预览URL
   * @param {File} file - 文件对象
   * @returns {Promise<string>} 预览URL
   */
  static createPreviewUrl(file) {
    return new Promise((resolve, reject) => {
      if (!file) {
        reject(new Error('文件对象为空'))
        return
      }

      if (!file.type.startsWith('image/')) {
        reject(new Error('不是图片文件'))
        return
      }

      const reader = new FileReader()
      reader.onload = (e) => {
        resolve(e.target.result)
      }
      reader.onerror = () => {
        reject(new Error('文件读取失败'))
      }
      reader.readAsDataURL(file)
    })
  }

  /**
   * 格式化文件大小
   * @param {number} bytes - 字节数
   * @returns {string} 格式化后的文件大小
   */
  static formatFileSize(bytes) {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  }

  /**
   * 获取文件扩展名
   * @param {string} filename - 文件名
   * @returns {string} 扩展名
   */
  static getFileExtension(filename) {
    return filename.slice((filename.lastIndexOf(".") - 1 >>> 0) + 2)
  }

  /**
   * 生成唯一文件名
   * @param {string} originalName - 原始文件名
   * @param {string|number} patientId - 患者ID
   * @param {string} type - 文件类型
   * @returns {string} 唯一文件名
   */
  static generateUniqueFileName(originalName, patientId, type = 'report') {
    const timestamp = new Date().getTime()
    const random = Math.random().toString(36).substring(2, 8)
    const extension = this.getFileExtension(originalName)
    return `${timestamp}_${patientId}_${type}_${random}.${extension}`
  }

  /**
   * 下载文件到本地
   * @param {File} file - 文件对象
   * @param {string} fileName - 保存的文件名
   */
  static downloadFile(file, fileName) {
    try {
      const url = URL.createObjectURL(file)
      const link = document.createElement('a')
      link.href = url
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)

      return {
        success: true,
        message: '文件下载成功'
      }
    } catch (error) {
      console.error('文件下载失败:', error)
      return {
        success: false,
        message: '文件下载失败'
      }
    }
  }

  /**
   * 压缩图片（简单的质量压缩）
   * @param {File} file - 图片文件
   * @param {number} quality - 质量 0-1
   * @param {number} maxWidth - 最大宽度
   * @param {number} maxHeight - 最大高度
   * @returns {Promise<Blob>} 压缩后的文件
   */
  static compressImage(file, quality = 0.8, maxWidth = 1920, maxHeight = 1080) {
    return new Promise((resolve, reject) => {
      if (!file.type.startsWith('image/')) {
        reject(new Error('不是图片文件'))
        return
      }

      const canvas = document.createElement('canvas')
      const ctx = canvas.getContext('2d')
      const img = new Image()

      img.onload = () => {
        // 计算新尺寸
        let { width, height } = img
        if (width > maxWidth || height > maxHeight) {
          const ratio = Math.min(maxWidth / width, maxHeight / height)
          width *= ratio
          height *= ratio
        }

        canvas.width = width
        canvas.height = height

        // 绘制压缩后的图片
        ctx.drawImage(img, 0, 0, width, height)

        // 转换为Blob
        canvas.toBlob((blob) => {
          resolve(blob)
        }, file.type, quality)
      }

      img.onerror = () => {
        reject(new Error('图片加载失败'))
      }

      img.src = URL.createObjectURL(file)
    })
  }
}