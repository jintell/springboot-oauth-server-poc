import React from 'react';
import { gql, useSubscription } from '@apollo/client';
import './stock.css';

const STOCK_SUBSCRIPTION = gql`
    subscription OnStockPriceUpdates($symbol: String!) {
        stockPriceUpdates(symbol: $symbol) {
            name
            symbol
            price
        }
    }
`;

export const StockTicker = ({ symbol }: { symbol: string }) => {
    const { data, loading } = useSubscription(STOCK_SUBSCRIPTION, {
        variables: { symbol },
    });

    if (loading) return <p>Loading...</p>;

    return (
        <div className="stock-name">
            <div>
                <span style={{ fontWeight: 'bold'}}>Stock Name: </span>
                <span style={{color: 'red', fontSize: 'larger', fontWeight: 'bold'}}>{data.stockPriceUpdates.name}</span>
            </div>
            <h2>({data.stockPriceUpdates.symbol}) {data.stockPriceUpdates.name}
                - : <span style={{ color: 'blue'}}>${data.stockPriceUpdates.price.toFixed(2)}</span></h2>
        </div>
    );
};
