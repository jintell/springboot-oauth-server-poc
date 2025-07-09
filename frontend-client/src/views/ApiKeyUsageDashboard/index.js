import React, { useState } from 'react';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';
import axios from 'axios';
import { saveAs } from 'file-saver';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

export const ApiKeyUsageDashboard = () => {
    const [apiKey, setApiKey] = useState('');
    const [adminKey, setAdminKey] = useState('');
    const [usageData, setUsageData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [minutesBack, setMinutesBack] = useState(60);
    const [keysSummary, setKeysSummary] = useState([]);

    const fetchUsage = async () => {
        setLoading(true);
        try {
            const res = await axios.get(`http://localhost:8080/api/v1/admin/usage/${apiKey}?minutesBack=${minutesBack}`, {
                headers: {
                    'X-API-KEY': adminKey
                }
            });
            setUsageData(res.data);
        } catch (err) {
            alert('Failed to fetch data. Check API key and admin key.');
        }
        setLoading(false);
    };

    const fetchKeysSummary = async () => {
        setLoading(true);
        try {
            const res = await axios.get(`http://localhost:8080/api/v1/admin/usage-summary`, {
                headers: {
                    'X-API-KEY': adminKey
                }
            });
            setKeysSummary(res.data);
        } catch (err) {
            alert('Failed to fetch API key summaries.');
        }
        setLoading(false);
    };

    const exportToCSV = () => {
        const headers = 'Timestamp,Count\n';
        const rows = usageData.map(d => `${d.timestamp},${d.count}`).join('\n');
        const csv = headers + rows;
        const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' });
        saveAs(blob, `usage-${apiKey}.csv`);
    };

    const chartData = {
        labels: usageData.map(d => d.timestamp),
        datasets: [
            {
                label: 'Requests per Minute',
                data: usageData.map(d => d.count),
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.2
            }
        ]
    };

    return (
        <div className="max-w-3xl mx-auto mt-10 p-4 bg-white rounded-xl shadow-md">
            <h2 className="text-2xl font-bold mb-4">API Key Usage Dashboard</h2>

            <div className="flex flex-col gap-4 mb-6">
                <input
                    className="p-2 border rounded"
                    placeholder="API Key (e.g. key-123)"
                    value={apiKey}
                    onChange={e => setApiKey(e.target.value)}
                />
                <input
                    className="p-2 border rounded"
                    placeholder="Admin Key (X-ADMIN-API-KEY)"
                    value={adminKey}
                    onChange={e => setAdminKey(e.target.value)}
                />
                <input
                    type="number"
                    className="p-2 border rounded"
                    placeholder="Minutes Back"
                    value={minutesBack}
                    onChange={e => setMinutesBack(Number(e.target.value))}
                />
                <div className="flex gap-2">
                    <button
                        onClick={fetchUsage}
                        style={{ background: '#0000EE', color: 'white', backgroundClip: '10px', cursor: 'pointer'}}
                        className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded"
                        disabled={loading}
                    >
                        {loading ? 'Loading...' : 'Fetch Usage'}
                    </button>
                    <button
                        onClick={fetchKeysSummary}
                        style={{ background: '#0000EE', color: 'white', backgroundClip: '10px', margin: '0 5px', cursor: 'pointer'}}
                        className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded"
                        disabled={loading}
                    >
                        {loading ? 'Loading...' : 'Fetch All Key Summaries'}
                    </button>
                    {usageData.length > 0 && (
                        <button
                            style={{ background: '#EE6600', color: 'white', backgroundClip: '10px', cursor: 'pointer'}}
                            onClick={exportToCSV}
                            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded"
                        >
                            Export CSV
                        </button>
                    )}
                </div>
            </div>

            {usageData.length > 0 && (
                <Line data={chartData} options={{ responsive: true, plugins: { legend: { position: 'bottom' } } }} />
            )}

            {keysSummary.length > 0 && (
                <div className="mt-6">
                    <h3 className="text-xl font-semibold mb-2">All API Key Usage Summary</h3>
                    <table className="w-full table-auto border">
                        <thead>
                        <tr>
                            <th className="border px-2 py-1">API Key</th>
                            <th className="border px-2 py-1">Owner</th>
                            <th className="border px-2 py-1">Requests (last hour)</th>
                        </tr>
                        </thead>
                        <tbody>
                        {keysSummary.map((row, idx) => (
                            <tr key={idx} className="border">
                                <td className="border px-2 py-1">{row.key}</td>
                                <td className="border px-2 py-1">{row.owner}</td>
                                <td className="border px-2 py-1">{row.total}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
