import { createSlice, current } from "@reduxjs/toolkit";

import { enableMapSet } from 'immer';

// Call enableMapSet() to enable the plugin for Map and Set data structures
enableMapSet();

const chatSlice  = createSlice({
   name: 'chatSlice',
   initialState: {
        privateChats: new Map()
   },
   reducers :{
    
        setPrivateChats: (state, action) => {
            state.privateChats = action.payload
        },
        addNewMessage: (state, action) => {
            enableMapSet();
            const temp = state.privateChats.get(action.payload.key)
            state.privateChats.set(action.payload.key, [...temp, action.payload.value])
            console.log(current(state))
            console.log(action)
            
        }
   },
   middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
})

export default chatSlice;

export const getPrivateChats = state => state.chatSlice.privateChats