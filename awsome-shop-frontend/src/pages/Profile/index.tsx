import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Alert from '@mui/material/Alert';
import { authService } from '../../services/authService';

export default function Profile() {
  const { t } = useTranslation();
  const [email, setEmail] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [saving, setSaving] = useState(false);
  const [msg, setMsg] = useState<{ type: 'success' | 'error'; text: string } | null>(null);

  useEffect(() => {
    authService.getCurrentUser()
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => {
        const d = res?.data ?? res;
        setEmail(d?.email || '');
        setDisplayName(d?.displayName || '');
      })
      .catch(() => {});
  }, []);

  const handleSave = async () => {
    setSaving(true);
    setMsg(null);
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      await (authService as any).updateProfile({ email, displayName });
      setMsg({ type: 'success', text: t('profile.saved') });
    } catch {
      setMsg({ type: 'error', text: t('profile.saveFailed') });
    } finally {
      setSaving(false);
    }
  };

  return (
    <Box sx={{ p: '24px 32px', maxWidth: 600 }}>
      <Typography sx={{ fontSize: 24, fontWeight: 700, mb: 3 }}>{t('profile.title')}</Typography>
      {msg && <Alert severity={msg.type} sx={{ mb: 2 }} onClose={() => setMsg(null)}>{msg.text}</Alert>}
      <Paper elevation={0} sx={{ p: 3, borderRadius: 3, border: '1px solid #F1F5F9', display: 'flex', flexDirection: 'column', gap: 2 }}>
        <TextField label={t('profile.displayName')} value={displayName} onChange={(e) => setDisplayName(e.target.value)} fullWidth size="small" data-testid="profile-displayname" />
        <TextField label={t('profile.email')} value={email} onChange={(e) => setEmail(e.target.value)} fullWidth size="small" type="email" placeholder="your@email.com" data-testid="profile-email" />
        <Button variant="contained" onClick={handleSave} disabled={saving} data-testid="profile-save" sx={{ alignSelf: 'flex-start', textTransform: 'none' }}>
          {t('profile.save')}
        </Button>
      </Paper>
    </Box>
  );
}
