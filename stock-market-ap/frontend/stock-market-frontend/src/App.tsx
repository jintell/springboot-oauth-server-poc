import React from 'react';
import { ApolloProvider } from '@apollo/client';
import './App.css';
import {StockTicker} from "./StockTicker";
import {client} from "./client";
import {StockList} from "./StockList";

function App() {
    // This is for quick demo purposes only.
    const [symbol, setSymbol] = React.useState("br");
  return (
      <ApolloProvider client={client} >
          <div className="App">
              <div className="marquee">
                  <span>Get real-time updates on your current stock prices</span>
              </div>
              <div className="App-Content">
                  <StockList setSymbol={setSymbol}/>
                  <StockTicker symbol={symbol} />
              </div>
          </div>
      </ApolloProvider>
  );
}

export default App;
