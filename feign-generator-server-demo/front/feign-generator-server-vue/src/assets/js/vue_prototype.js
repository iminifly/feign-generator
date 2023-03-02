import Vue from 'vue'
import { MessageBox } from 'element-ui'
import axios from 'axios';
Vue.use({
    install(Vue) {
        Vue.prototype.$axios = axios
        // alert
        Vue.prototype.$$alert = function (message, type) {
            return MessageBox.alert(message, '系统提示', {
                confirmButtonText: '确定',
                type
            })
        }
        // format dateTime
        Vue.prototype.$$dateTimeFormat = function (dateTime) {
            const year = dateTime.getFullYear()
            const month = format(dateTime.getMonth() + 1)
            const date = format(dateTime.getDate())
            const hour = format(dateTime.getHours())
            const minute = format(dateTime.getMinutes())
            const second = format(dateTime.getSeconds())
            return year + '-' + month + '-' + date + ' ' + hour + ':' + minute + ':' + second
        }
        // format date
        Vue.prototype.$$dateFormat = function (date) {
            const year = date.getFullYear()
            const month = format(date.getMonth() + 1)
            const day = format(date.getDate())
            return year + '-' + month + '-' + day
        }
    }
})

function format (num) {
    if (num < 10) {
        return '0' + num
    }
    return '' + num
}
