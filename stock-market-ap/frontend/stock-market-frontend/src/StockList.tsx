
const stocks = [
    {symbol: 'apple', name: 'Apple Inc.'},
    {symbol: 'gl', name: 'Gold'},
    {symbol: 'br', name: 'Bronze'},
    {symbol: 'goggle', name: 'Goggle Inc.'},
]

export const StockList = ({setSymbol} : {setSymbol: any}  ) => {

    const onSelectStock = (event: { target: { value: any; }; }) => {
        setSymbol(event.target.value);
    }
    return (
        <div className="stock-menu">
            <span style={{ fontWeight: 'bold'}}>Stock List: </span>
            <select name="stocks"
                    id="stocks"
                    onChange={onSelectStock}>
                <option value="">Select a stock</option>
                {stocks.map(stock => (
                    <option key={stock.symbol} value={stock.symbol}>{stock.symbol} - {stock.name}</option>
                ))}
            </select>
        </div>
    )
}