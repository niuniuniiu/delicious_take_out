// 查询列表页接口
const getOrderDetailPage = (params) => {
  return $axios({
    url: '/order/page',
    method: 'get',
    params
  })
}


// 获取订单详情 + 菜品信息
const queryOrderDetailById = (id) => {
  return $axios({
    url: `/order/${id}`,
    method: 'get'
  })
}


// 取消，派送，完成接口
const editOrderDetail = (params) => {
  return $axios({
    url: '/order',
    method: 'put',
    data: { ...params }
  })
}
