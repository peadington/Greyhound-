export const config = {
  // apiUrl: 'http://159.65.157.19/greyhound/',
  //apiUrl: 'http://localhost:8080/api/v1/',
  // apiUrl: 'http://192.168.29.218:8080/api/v1/',
  apiUrl: 'http://167.99.14.166:8080/api/v1/',

  authRoles: {
    sa: ['SA'], // Only Super Admin has access
    admin: ['SA', 'Admin'], // Only SA & Admin has access
    editor: ['SA', 'Admin', 'Editor'], // Only SA & Admin & Editor has access
    user: ['SA', 'Admin', 'Editor', 'User'], // Only SA & Admin & Editor & User has access
    guest: ['SA', 'Admin', 'Editor', 'User', 'Guest'] // Everyone has access
  }
}
