
//observable用于创建一个被检测的对象，对象的属性就是应用的状态
//actions 用于定义actions方法，action方法 用于修改 更新状态
import {observable, action} from "mobx-miniprogram"

export const numStore = observable({
  num1:10,
  num2:20,

  update:action(function(){
    this.num1 += 1
    this.num2 += 1
  }),

  //计算属性必须要用get 修饰 并有返回值
  get sum(){
    return this.num1 + this.num2
  }
})
