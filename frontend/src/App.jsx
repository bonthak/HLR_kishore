import React from 'react';
import SubscriberCreateForm from './components/SubscriberCreateForm';
import SubscriberSearchPanel from './components/SubscriberSearchPanel';
import ApprovalPanel from './components/ApprovalPanel';

export default function App() {
  return (
    <div className="layout">
      <header>
        <h1>HLR Provisioning Console</h1>
        <p>Subscriber lifecycle, identity mapping, roaming policy, and maker-checker workflow.</p>
      </header>
      <main>
        <SubscriberCreateForm />
        <SubscriberSearchPanel />
        <ApprovalPanel />
      </main>
    </div>
  );
}
