import React, { useState } from 'react';
import { createSubscriber } from '../api/client';

const initialState = {
  externalRef: '',
  market: 'IN',
  imsi: '',
  msisdn: '',
  iccid: '',
  baoc: false,
  boic: false,
  baic: false,
  clip: true,
  clir: false,
  callWaiting: true,
  gprsEnabled: true,
  apnProfile: 'default',
  camelProfileVersion: 'v1',
  roamingEnabled: false,
  allowedVplmnCsv: '',
  barredVplmnCsv: '',
  regionClass: 'DEFAULT'
};

export default function SubscriberCreateForm({ onCreated }) {
  const [form, setForm] = useState(initialState);
  const [status, setStatus] = useState('');

  const onChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
  };

  const submit = async (e) => {
    e.preventDefault();
    try {
      const res = await createSubscriber(form);
      setStatus(`Created subscriber ${res.data.externalRef}`);
      setForm(initialState);
      onCreated?.(res.data);
    } catch (err) {
      setStatus(err.response?.data?.message || 'Creation failed');
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>Create Subscriber</h2>
      <div className="grid">
        <input name="externalRef" value={form.externalRef} onChange={onChange} placeholder="Subscriber Ref" required />
        <input name="market" value={form.market} onChange={onChange} placeholder="Market" required />
        <input name="imsi" value={form.imsi} onChange={onChange} placeholder="IMSI" required />
        <input name="msisdn" value={form.msisdn} onChange={onChange} placeholder="MSISDN" required />
        <input name="iccid" value={form.iccid} onChange={onChange} placeholder="ICCID" required />
        <input name="apnProfile" value={form.apnProfile} onChange={onChange} placeholder="APN Profile" required />
        <input name="camelProfileVersion" value={form.camelProfileVersion} onChange={onChange} placeholder="CAMEL Version" required />
        <input name="allowedVplmnCsv" value={form.allowedVplmnCsv} onChange={onChange} placeholder="Allowed VPLMN CSV" />
      </div>
      <div className="checks">
        <label><input type="checkbox" name="roamingEnabled" checked={form.roamingEnabled} onChange={onChange} /> Roaming</label>
        <label><input type="checkbox" name="baoc" checked={form.baoc} onChange={onChange} /> BAOC</label>
        <label><input type="checkbox" name="boic" checked={form.boic} onChange={onChange} /> BOIC</label>
        <label><input type="checkbox" name="baic" checked={form.baic} onChange={onChange} /> BAIC</label>
      </div>
      <button type="submit">Create</button>
      <p>{status}</p>
    </form>
  );
}
