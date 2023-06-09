import { apiClient } from "./ApiClient";

export const sendPublicMessage = (message) => apiClient.post('/api/send/public', message)

export const sendMessagePrivate = (message) => apiClient.post('/api/send/private', message)

export const getMessagePrivate = (username,sender) => apiClient.get(`/api/messages/${username}/${sender}`)

export const registerNewUser = (userName) => apiClient.post('/api/users/register',{userName})