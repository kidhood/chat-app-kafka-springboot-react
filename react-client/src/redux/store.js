import { configureStore } from "@reduxjs/toolkit";
import chatSlice from "./chatSlice";



const store = configureStore({
   reducer: {
    chatSlice: chatSlice.reducer,
    
   },
   middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
})
export default store;