const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  roleId: state => state.user.roleId,
  avatar: state => state.user.avatar,
  id: state => state.user.id,
  name: state => state.user.name,
  menus: state => state.user.menus,
  permission_routes: state => state.permission.routes

}
export default getters
