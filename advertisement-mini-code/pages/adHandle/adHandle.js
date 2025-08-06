// pages/adDetail/adDetail.js

import { getAdDetail,handleAdInfo } from "@/api/adHandle";
import { baseURL } from "@/utils/apiUrl";

Page({
  data: {
    baseURL:baseURL,
    roleKey: wx.getStorageSync('roleKey'),
    surveyor:wx.getStorageSync("username"),
    // 轮播图数据
    adImages: [
      // 'https://placehold.co/640x300?text=Ad1',
      // 'https://placehold.co/640x300?text=Ad2',
      // 'https://placehold.co/640x300?text=Ad3'
    ],
    // 广告详情数据
    adInfo: {
      createTime: '',
      province: '',
      city: '',
      district: '',
      street: '',
      address:"",
      longitude:'',
      latitude:'',
      adDescription:"",

      processingStatus: '已拆除',
      handleResult:'',
      afterProcessingImages: [
        'https://placehold.co/100x100?text=After1',
        'https://placehold.co/100x100?text=After2'
      ]
    },
    // 上传图片状态
    uploadStatus: 'ready',
    uploadProgress: 0,
    activeNames: ['auditOpinion'], // 默认展开审批意见面板
    handleImgList:[],
    handleType:'请选择处理情况',
    handleList: ["已拆除","已罚款"],
    //广告id
    id:"",
  },
  onLoad(options) {
    // 页面加载时初始化数据
    this.setData({
      adInfo: this.data.adInfo
    });
    console.log("页面加载options",options)
    const { id } = options;
    if (id){
      this.setData({
        id:id
      })
      getAdDetail(id).then(res=>{
        console.log('获取到广告详情数据', res);
        //具体规定
        //具体规定的哪一条

        //如果具体规定和条数都有值同时都没有，那么直接取lawName和clauseNumber组合成一条
        //否则就把lawName和clauseNumber用,分割成数组，然后两两对应拼接成一条规定
        if(res.data.auditRecordsList.length>0){
          res.data.auditRecordsList.forEach(record=>{
            // 检查lawName和clauseNumber是否存在且不为空
            if(record.lawName && record.clauseNumber){
              // 将lawName和clauseNumber按逗号分割成数组
              const lawNames = record.lawName.split(',');
              const clauseNumbers = record.clauseNumber.split(',');
              
              // 确保两个数组长度一致
              if(lawNames.length === clauseNumbers.length){
                // 组合对应的法律名称和条款号，生成用逗号分隔的字符串
                record.content = lawNames.map((law, index) => `${law}${clauseNumbers[index]}`).join(', ');
              } else {
                // 如果长度不一致，回退到原来的处理方式
                record.content = `无涉及法规，广告合法`;
              }
            } else {
              // 如果lawName或clauseNumber任一为空，保持原逻辑
              record.content = `无涉及法规，广告合法`;
            }
          });
        }
        console.log(res.data,123456)

        //处理审核相关数据 
        this.setData({
          adInfo: res.data
        })

        if(res.data.adImages) {
          const images = res.data.adImages.split(',');
          //数组长度大于0 表示多图，如果是单图不需要处理
          if(images.length > 0){

            console.log("多图")
            this.setData({
              adImages: images
            })
          } else {
            console.log("单图")

            this.setData({
              adImages: [res.data.adImages]
            })
          }
        }

        if(res.data.enforcement.postHandleImage) {
          const postHandleImage = res.data.enforcement.postHandleImage.split(',');
          //处理图片
          if(postHandleImage.length > 0){
            this.setData({
              afterProcessingImages: postHandleImage
            })
          } else {
            this.setData({
              afterProcessingImages: [res.data.enforcement.postHandleImage]
            })
          }
        }
        
      })
    }
  },

  //去导航页面
  goNavigation() {
    // console.log(this.data.campDetail);
    const latitude = this.data.adInfo.latitude
    const longitude = this.data.adInfo.longitude
    // console.log(latitude,longitude);
    // 调用微信官方的函数跳转到腾讯地图页面
    wx.openLocation({
      latitude: Number(latitude), // 纬度
      longitude: Number(longitude), // 经度
      scale: 28, // 缩放比例
      name: "广告位置", // 位置名
      address: this.data.adInfo.address
    });
  },
  // 违法状态变更处理函数
  handleCollapseChange(e) {
    this.setData({
      activeNames: e.detail.value
    });
  },
  // 处理情况变更处理函数
  handleSituation(){
    //这里使用showActionSheet
    wx.showActionSheet({
      itemList: this.data.handleList,
      success: (res) =>{
        console.log(res.tapIndex);
        // 选择了违法类别
        this.setData({
          handleType: this.data.handleList[res.tapIndex],
          handleResult: res.tapIndex + 1
        })
      },
      fail: function (res) {
        console.log(res);
      }
    });
  },
  // 上传图片
  uploadPhoto(){


    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      sizeType: ['compressed'],
      camera: 'back',
      success:(res) => {
        console.log(res,7878)
        const tempFilePaths = res.tempFiles[0].tempFilePath;
        const adPositionValue = this.data.adInfo.province + this.data.adInfo.city + this.data.adInfo.district +this.data.adInfo.street
        const address = this.data.adInfo.address
        const surveyor = this.data.surveyor
        wx.uploadFile({
          url: `${baseURL}/common/uploadFileAddWaterMark?xaddress=${adPositionValue}&xsurveyor=${surveyor}&xlocation=${address}`,
          filePath: tempFilePaths,
          header: {
            "Authorization": `Bearer ${wx.getStorageSync('token')}`,
            "Content-Type": "multipart/form-data",
          },
          name: 'file',
          success: (uploadRes) => {

            const url = JSON.parse(uploadRes.data).url;
            console.log('上传成功', url);
            // 上传成功后，将图片路径添加到 adImages 中
            const { handleImgList } = this.data;
            this.setData({
              handleImgList:[...handleImgList,url]
            });
          },
          fail: (uploadErr) => {
            console.error('上传失败:', uploadErr);
            if (uploadErr.statusCode === 413) {
              wx.showToast({
                title: '图片过大，请选择较小的图片',
                icon: 'none'
              });
            } else {
              wx.showToast({
                title: '上传失败',
                icon: 'none'
              });
            }
          }
        });
      }
    })
  },
  // 预览图片
  previewImage(e){
    let {image} = e.currentTarget.dataset
    wx.previewImage({
      urls: [image],
    })
  },
  // 删除图片
  deletePhoto(e){
    const index = e.currentTarget.dataset.index;
    const { handleImgList } = this.data;
    handleImgList.splice(index, 1);
    this.setData({
      handleImgList
    });
  },
  //执法者提交处理情况
  submitHandle(){
    let {id,handleResult,handleImgList} = this.data

    if(!id || !handleResult || handleImgList.lenth) {
      wx.showToast({
        title: '请填写完整信息',
        icon: 'none'
      });
      return
    } else {
      handleAdInfo({
        adId:id,
        handleResult:handleResult,
        postHandleImage:handleImgList.join(',')
      }).then(res=>{
        if (res.code==200) {
          wx.showToast({
            title: res.msg,
            icon: 'success'
          });
          setTimeout(()=>{
            wx.reLaunch({
              url: '/pages/enforcementList/enforcementList',
            });
          },400)
        }else{
          wx.showToast({
            title: res.msg,
            icon: 'none'
          });
        } 
      })
    }

  }
})