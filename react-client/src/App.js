import React from 'react'
import ChatRoom from './components/ChatRoom'
import { Provider } from 'react-redux';
import store from './redux/store';

const App = () => {
  return (
   
    <Provider store={store}>

        <ChatRoom />
      </Provider>
  )
}

export default App;

