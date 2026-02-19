import React, { useState } from 'react';
import { searchSubscriber, changeState, simSwap, numberChange } from '../api/client';

export default function SubscriberSearchPanel() {
  const [key, setKey] = useState('');
  const [rows, setRows] = useState([]);
  const [status, setStatus] = useState('');

  const search = async () => {
    try {
      const res = await searchSubscriber(key);
      setRows(res.data);
      setStatus(`${res.data.length} result(s)`);
    } catch (err) {
      setStatus(err.response?.data?.message || 'Search failed');
    }
  };

  const updateState = async (id, state) => {
    await changeState(id, state);
    await search();
  };

  const runSimSwap = async (id, oldImsi) => {
    const newImsi = prompt('New IMSI');
    const newIccid = prompt('New ICCID');
    if (!newImsi || !newIccid) return;
    await simSwap(id, { oldImsi, newImsi, newIccid, actor: 'admin' });
    await search();
  };

  const runNumberChange = async (id, oldMsisdn) => {
    const newMsisdn = prompt('New MSISDN');
    if (!newMsisdn) return;
    await numberChange(id, { oldMsisdn, newMsisdn, actor: 'admin' });
    await search();
  };

  return (
    <section className="card">
      <h2>Subscriber Search</h2>
      <div className="row">
        <input value={key} onChange={(e) => setKey(e.target.value)} placeholder="IMSI / MSISDN / ICCID / External Ref" />
        <button onClick={search}>Search</button>
      </div>
      <p>{status}</p>
      <div className="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Ref</th>
              <th>State</th>
              <th>IMSI</th>
              <th>MSISDN</th>
              <th>Roaming</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((r) => (
              <tr key={r.id}>
                <td>{r.externalRef}</td>
                <td>{r.state}</td>
                <td>{r.activeImsi}</td>
                <td>{r.activeMsisdn}</td>
                <td>{String(r.roamingEnabled)}</td>
                <td>
                  <button onClick={() => updateState(r.id, 'ACTIVE')}>Activate</button>
                  <button onClick={() => updateState(r.id, 'SUSPENDED')}>Suspend</button>
                  <button onClick={() => runSimSwap(r.id, r.activeImsi)}>SIM Swap</button>
                  <button onClick={() => runNumberChange(r.id, r.activeMsisdn)}>Number Change</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
