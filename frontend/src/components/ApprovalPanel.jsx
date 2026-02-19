import React, { useState } from 'react';
import { approveChange, createChangeRequest, listPending } from '../api/client';

export default function ApprovalPanel() {
  const [payload, setPayload] = useState('{"change":"example"}');
  const [subscriberId, setSubscriberId] = useState('');
  const [status, setStatus] = useState('');
  const [pending, setPending] = useState([]);

  const submit = async () => {
    try {
      await createChangeRequest({
        subscriberId: subscriberId || null,
        operationType: 'ROAMING_TOGGLE',
        riskLevel: 'HIGH',
        requestedBy: 'provisioner_user',
        payload
      });
      setStatus('Change request submitted');
      await loadPending();
    } catch (err) {
      setStatus(err.response?.data?.message || 'Submit failed');
    }
  };

  const loadPending = async () => {
    const res = await listPending();
    setPending(res.data);
  };

  const handleApproval = async (id, approved) => {
    await approveChange(id, { approver: 'approver_user', approved });
    await loadPending();
  };

  return (
    <section className="card">
      <h2>Maker-Checker</h2>
      <div className="grid">
        <input value={subscriberId} onChange={(e) => setSubscriberId(e.target.value)} placeholder="Subscriber UUID (optional)" />
        <textarea value={payload} onChange={(e) => setPayload(e.target.value)} rows={3} />
      </div>
      <div className="row">
        <button onClick={submit}>Create Change Request</button>
        <button onClick={loadPending}>Refresh Pending</button>
      </div>
      <p>{status}</p>
      <ul>
        {pending.map((item) => (
          <li key={item.id}>
            {item.operationType} | {item.riskLevel} | requested by {item.requestedBy}
            <button onClick={() => handleApproval(item.id, true)}>Approve</button>
            <button onClick={() => handleApproval(item.id, false)}>Reject</button>
          </li>
        ))}
      </ul>
    </section>
  );
}
