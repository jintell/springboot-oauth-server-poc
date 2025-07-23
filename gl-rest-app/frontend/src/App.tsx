import React, { useState } from 'react';
import axios from 'axios';
import {ApolloProvider} from "@apollo/client";
import {client} from "./client/client";
import {StockTicker} from "./stock/StockTicker";

const App = () => {
  const [symbol, setSymbol] = useState('apl');
  const [data, setData] = useState<any>(null);

  const fetchInsight = async () => {
    try {
      const res = await axios.get(`http://localhost:9100/v1/stocks/${symbol}/insight/metrics`, {
        params: { "q": "symbol,name,marketCap,peRatio,revenueGrowth,price" }
      });
      setData(res.data);
    } catch (err) {
      console.error("Error fetching data", err);
    }
  };

  return (
      <ApolloProvider client={client} >
              <div style={{ padding: '2rem', fontFamily: 'sans-serif' }}>
                <h1>📈 Stock Insight Viewer</h1>

                <div>
                  <input value={symbol} onChange={e => setSymbol(e.target.value)} placeholder="Stock Symbol" />
                  <button onClick={fetchInsight}>Get Insight</button>
                </div>

                {data && (
                    <div style={{ marginTop: '2rem' }}>
                      <h2>Insight for: {data.symbol} - {data.name}</h2>
                      <ul>
                        <li>Market Cap: ${data.marketCap?.toLocaleString()}</li>
                        <li>P/E Ratio: {data.peRatio}</li>
                        <li>Revenue Growth: {data.revenueGrowth}%</li>
                        <li>Original Price: {data.price.toFixed(2)}</li>
                      </ul>
                    </div>
                )}
                  <div>
                      <StockTicker symbol={symbol} />
                  </div>
              </div>
          </ApolloProvider>
  );
};

export default App;