// pages/upload/upload.js
import { getIndustryList,checkIndustry,addIndustry,getMediaTypeList,checkMediaType,addMediaType,uploadAdvertisement } from '@/api/upload.js'
Page({
  data: {
    adProfitabilityType: '1', // 盈利类型
    adIndustryType: '', // 行业
    adMediumType: '',   //媒体
    adPositionValue: "", //广告位置
    address: '',//详细地址
    adDescription: '',//广告描述
    advertiser: '', //广告主
    violationType: "", //违法类型
    illegalTypeValue: '', //违法类型值
    adImages: [], // 上传的图片列表
    latitude: "", //维度
    longitude: "", //经度

    surveyor:"",
    surveyorId:"",

    //行业
    visible:false,  //行业分类弹框
    showWithIndustryInput: false, //新增行业弹窗
    inputIndustryValue:"", // 输入的行业内容
    tagList:[],
    selectedTags:[],

    // 媒体类型相关数据
    mediaTypeVisible: false, //媒体类型弹窗
    showWithMediaInput: false, //新增 媒体类型弹窗
    inputMediaValue:"", //新增媒体类型弹窗
    mediaTypeTagList: [],
    selectedMediaTypeTags: [],

    // 违法类型相关数据
    illegalList: ["禁用词类", "虚假宣传", "低俗类", "敏感类", "其他类"],
  },
  onLoad(option){
    this.setData({
      violationType:option.violationType,
      illegalTypeValue:option.illegalTypeValue,
      surveyor:wx.getStorageSync("username"),
      surveyorId:wx.getStorageSync("userId"),
    })
    //获取行业类型列表
    this.getIndustryListData()
    //获取媒体类型列表
    this.getMediaTypeListData()
  
  },

  // -------------行业-----------------
  //获取行业列表
  getIndustryListData(){
    return getIndustryList().then(res=>{
      console.log(res.data,"获取行业分类数据")
      this.setData({
        tagList:res.data
      })
      return res;
    })
  },
  // 显示行业分类弹框
  navigateToIndustry() {
    this.setData({
      visible: true
    })
  },
  // 关闭行业弹框
  closeIndustryPopup(){
    let adIndustryType = this.data.selectedTags.join(",")
    this.setData({
      visible: false,
      adIndustryType: adIndustryType
    })
    // 重新获取行业列表
    this.getIndustryListData()
  },
  // 选择行业标签
  handleIndustryTagChange(e){
    const {checked} = e.detail
    const {selectedTag} = e.currentTarget.dataset
    // 选中标签
    if(checked){
      this.setData({
        selectedTags:[...this.data.selectedTags,selectedTag]
      })
    }else{
      // 取消选中标签
      this.setData({
        selectedTags:this.data.selectedTags.filter(item=>item!==selectedTag)
      })
    }
    // console.log(this.data.selectedTags)
  },
  // 关闭行业标签
  handleIndustryTagClose(e){
    //点击哪个标签就在tagList中去掉哪个标签，同时在selectedTags中去掉哪个标签
    const {selectedTag} = e.currentTarget.dataset
    this.setData({
      selectedTags:this.data.selectedTags.filter(item=>item!==selectedTag),
      tagList:this.data.tagList.filter(item=>item!==selectedTag)
    })
    console.log(this.data.selectedTags)
    console.log(this.data.tagList)
  },
  // -------------行业end-----------------

  // --------------添加行业分类弹框---------------------
  // 展示添加行业弹框
  showIndustryDialog() {
    this.setData({
      showWithIndustryInput: true
    })
  },
  // 确定添加行业弹框
  confirmIndustryDialog() {
    if (this.data.inputIndustryValue) {
      // 调用接口判断是否存在该行业，如果不存在则添加
      checkIndustry(this.data.inputIndustryValue).then(res => {
        console.log(res.data, "添加行业");
        if (res.msg) {
          // 存在
          wx.showToast({
            title: '该行业已存在',
            icon: 'none'
          });
          this.setData({
            inputIndustryValue: ""
          });
          return;
        } else {
          // 不存在
          addIndustry(this.data.inputIndustryValue).then(res => {
            if (res.code === 200) {
              wx.showToast({
                title: '添加成功',
                icon: 'success'
              });
              //push到tagList中
              this.setData({
                tagList:[...this.data.tagList,res.msg],
                inputValue: "",
                showWithIndustryInput: false
              })
            }
          })
        }
      })
    }
  },
  // 取消添加行业弹框
  closeIndustryDialog() {
    this.setData({
      showWithIndustryInput: false,
      inputValue: ""
    });
  },
  // ---------------行业分类弹框end----------------------

  //获取媒体类型数据
  getMediaTypeListData(){
    return getMediaTypeList().then(res=>{
      console.log(res.data,"获取行业分类数据")
      this.setData({
        mediaTypeTagList:res.data
      })
      return res;
    })
  },
  // 显示媒体类型弹框
  navigateToMediaType() {
    this.setData({
      mediaTypeVisible: true
    })
  },
  // 关闭媒体类型弹框
  closeMediaTypePopup() {
    let adMediumType = this.data.selectedMediaTypeTags.join(",")
    this.setData({
      mediaTypeVisible: false,
      adMediumType: adMediumType
    })
    // 获取媒体类型列表
    this.getMediaTypeListData()
  },
  
  // 选择媒体类型标签
  handleMediaTypeTagChange(e) {
    const {checked} = e.detail
    const {selectedTag} = e.currentTarget.dataset
    // 选中标签
    if(checked){
      this.setData({
        selectedMediaTypeTags:[...this.data.selectedMediaTypeTags,selectedTag]
      })
    }else{
      // 取消选中标签
      this.setData({
        selectedMediaTypeTags:this.data.selectedMediaTypeTags.filter(item=>item!==selectedTag)
      })
    }
  },
  
  // 关闭媒体类型标签
  handleMediaTypeTagClose(e) {
    const {selectedTag} = e.currentTarget.dataset
    this.setData({
      selectedMediaTypeTags:this.data.selectedMediaTypeTags.filter(item=>item!==selectedTag),
      mediaTypeTagList:this.data.mediaTypeTagList.filter(item=>item!==selectedTag)
    })
  },
  

  // -------------添加媒体分类弹框---------------------
  // 展示添加媒体类型弹框
  showMediaTypeDialog() {
    this.setData({
      showWithMediaInput: true,
      // mediaTypeVisible: false
    })
  },
  
  // 确定添加媒体类型弹框
  confirmMediaDialog() {
    if (this.data.inputMediaValue) {
      // 调用接口判断是否存在该行业，如果不存在则添加
      checkMediaType(this.data.inputMediaValue).then(res => {
        console.log(res.data, "添加媒体类型");
        if (res.msg) {
          // 存在
          wx.showToast({
            title: '该媒体类型已存在',
            icon: 'none'
          });
          this.setData({
            inputMediaValue: ""
          });
          return;
        } else {
          // 不存在
          addMediaType(this.data.inputMediaValue).then(res => {
            if (res.code === 200) {
              wx.showToast({
                title: '添加成功',
                icon: 'success'
              });
              //push到mediaTypeTagList中
              this.setData({
                mediaTypeTagList:[...this.data.mediaTypeTagList,res.msg],
                inputMediaValue: "",
                showWithMediaInput: false
              })
            }
          })
        }
      })
    }
  },
  // 取消添加媒体类型弹框
  closeMediaDialog() {
    this.setData({
      showWithMediaInput: false,
      inputMediaValue: ""
    });
  },
  // -------------添加媒体分类弹框end-------------------
  
  

  //违法类别
  navigateToIllegalType() {
    //这里使用showActionSheet
    wx.showActionSheet({
      itemList: this.data.illegalList,
      success: (res) =>{
        console.log(res.tapIndex);
        // 选择了违法类别
        this.setData({
          illegalTypeValue: this.data.illegalList[res.tapIndex]
        })
      },
      fail: function (res) {
        console.log(res);
      }
    });
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      adProfitabilityType: tab
    });
    // 这里可以记录选中的状态
    console.log('选中的标签:', tab);
  },
  // 广告位置
  navigateToAdPosition() {
    // 检查用户权限状态
    wx.getSetting({
      success: res => {
        if (!res.authSetting['scope.userLocation']) {
          // 用户尚未授权或已拒绝授权
          // 主动请求授权
          this.requestLocationAuthorization();
        } else {
          // 已经授权，直接获取位置信息
          this.getUserLocation();
        }
      }
    })
  },
  // 请求授权
  requestLocationAuthorization() {
    // 请求用户授权精确位置
    wx.authorize({
      scope: 'scope.userLocation',
      success: () => {
        this.getUserLocation();
      },
      fail: (err) => {
        // 拒绝授权，提示用户收到开启
        this.showSettingPrompt()
      }
    })
  },
  // 提示用户开启授权
  showSettingPrompt() {
    wx.showModal({
      title: '需要位置权限',
      content: '请在设置中开启位置权限以正常使用本功能。',
      showCancel: false,
      confirmText: '去设置',
      success: (res) => {
        if (res.confirm) {
          wx.openSetting({
            success: (settingRes) => {
              if (settingRes.authSetting['scope.userLocation']) {
                // 用户在设置中开启了位置权限
                this.getUserLocation();
              } else {
                // 用户仍然拒绝位置权限
                console.log('用户仍未开启位置权限');
              }
            },
          });
        }
      },
    });
  },
  //获取位置信息
  getUserLocation() {
    wx.getLocation({
      type: 'gcj02',
      success: (res) => {
        console.log(res, "返回的数据")
        this.setData({
          latitude: res.latitude,
          longitude: res.longitude,
        });
        wx.chooseLocation({
          longitude: this.data.longitude,
          latitude: this.data.latitude,
          success: (res) => {
            console.log(res, '########');
            this.setData({
              latitude: res.latitude,
              longitude: res.longitude,
              adPositionValue: res.address
            });
          },
          fail: err => {
            console.log(err)
          }
        })
      },
      fail: (err) => {
        console.error('获取位置失败:', err);
      },
      complete: () => {
        console.log('位置获取操作已完成');
      }
    });
  },
  //上传广告图片
  uploadPhoto() {
    //判断用户是否选择了广告位置
    if (!this.data.adPositionValue && !this.data.address) {
      wx.showToast({
        title: '请先选择广告位置',
        icon: 'none'
      });
      return;
    }
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      sizeType: ['compressed'],
      camera: 'back',
      success: (res) => {
        console.log(res,7878)
        const tempFilePaths = res.tempFiles[0].tempFilePath;
        // 清理header中的变量值
        const adPositionValue = this.data.adPositionValue;
        const surveyor = this.data.surveyor;
        const address = this.data.address;
        console.log("上传广告图片:",adPositionValue,surveyor,address);
        wx.uploadFile({
          // url: 'https://admin.mcahu.ac.cn/common/upload',
          url: `http://127.0.0.1:8080/common/uploadFileAddWaterMark?xaddress=${adPositionValue}&xsurveyor=${surveyor}&xlocation=${address}`,
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
            const { adImages } = this.data;
            this.setData({
              adImages:[...adImages,url]
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
      },
      fail: (err) => {
        console.log('取消选择图片', err);
      }
    })
  },
  //删除图片
  deletePhoto(e) {
    const index = e.currentTarget.dataset.index;
    const { adImages } = this.data;
    adImages.splice(index, 1);
    this.setData({
      adImages
    });
  },
  //提交
  handleSubmitAd(){
    // 表单验证
    let { adProfitabilityType,adIndustryType,adMediumType,adPositionValue,address,adImages,adDescription,advertiser,violationType,latitude,longitude,surveyor,surveyorId} = this.data
    if(!adProfitabilityType || !adIndustryType || !adMediumType || !adPositionValue || !address || !adImages.length || !adDescription  || !violationType || !latitude || !longitude || !surveyor || !surveyorId) {
      wx.showToast({
        title: '请填写完整信息',
        icon: 'none'
      });
      console.log(adProfitabilityType,adIndustryType,adMediumType,adPositionValue,address,adImages,adDescription,advertiser,violationType,latitude,longitude,surveyor,surveyorId)
      return;
    } else {
      const adImages = this.data.adImages.join(",")
      uploadAdvertisement({
        adProfitabilityType,adIndustryType,adMediumType,adPositionValue,address,adImages,adDescription,advertiser,violationType,latitude,longitude,surveyor,surveyorId
      }).then(res=>{
        if (res.code==200) {
          wx.showToast({
            title: res.msg,
            icon: 'success'
          });
          setTimeout(()=>{
            wx.reLaunch({
              url: '/pages/index/index',
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
  },
  //图片预览
  previewImage(e){
    let {image} = e.currentTarget.dataset
    wx.previewImage({
      urls: [image],
    })
  }
})