import React from 'react';
import { gql, useSubscription } from '@apollo/client';

const STOCK_SUBSCRIPTION = gql`
    subscription OnStockPriceUpdates($symbol: String!) {
        updateStockInsightPrice(symbol: $symbol) {
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
                <span style={{color: 'red', fontSize: 'larger', fontWeight: 'bold'}}>{data.updateStockInsightPrice.name}</span>
            </div>
            <h2>({data.updateStockInsightPrice.symbol}) {data.updateStockInsightPrice.name}
                - : <span style={{ color: 'blue'}}>${data.updateStockInsightPrice.price.toFixed(2)}</span></h2>
        </div>
    );
};
