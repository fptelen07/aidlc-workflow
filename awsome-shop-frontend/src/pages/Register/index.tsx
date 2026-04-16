import { useState, useMemo } from 'react';
import { useNavigate } from 'react-router';
import { useTranslation } from 'react-i18next';
import { ThemeProvider } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Alert from '@mui/material/Alert';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import Link from '@mui/material/Link';
import PersonIcon from '@mui/icons-material/Person';
import LockIcon from '@mui/icons-material/Lock';
import BadgeIcon from '@mui/icons-material/Badge';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import RedeemIcon from '@mui/icons-material/Redeem';
import { authService } from '../../services/authService';
import { getTheme } from '../../theme';

export default function Register() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const lightTheme = useMemo(() => getTheme('light'), []);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [displayName, setDisplayName] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleRegister = async () => {
    setError('');
    if (!username || !password || !confirmPassword || !displayName) {
      setError(t('register.required'));
      return;
    }
    if (password.length < 6) {
      setError(t('register.passwordTooShort'));
      return;
    }
    if (password !== confirmPassword) {
      setError(t('register.passwordMismatch'));
      return;
    }
    setLoading(true);
    try {
      await authService.register(username, password, displayName);
      navigate('/login', { state: { registered: true } });
    } catch {
      setError(t('register.failed'));
    } finally {
      setLoading(false);
    }
  };

  const fieldSx = {
    height: 44, borderRadius: '8px', fontFamily: 'Inter, sans-serif',
    '& .MuiOutlinedInput-notchedOutline': { borderColor: '#E2E8F0' },
    '&:hover .MuiOutlinedInput-notchedOutline': { borderColor: '#CBD5E1' },
    '& input::placeholder': { color: '#CBD5E1', opacity: 1 },
  };

  return (
    <ThemeProvider theme={lightTheme}>
      <Box sx={{ display: 'flex', height: '100vh', width: '100vw' }}>
        {/* Left Brand Panel */}
        <Box sx={{ width: 640, flexShrink: 0, bgcolor: 'primary.main', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: 3, p: '60px' }}>
          <RedeemIcon sx={{ fontSize: 64, color: '#fff' }} />
          <Typography sx={{ color: '#fff', fontSize: 40, fontWeight: 700 }}>{t('login.brand')}</Typography>
          <Typography sx={{ color: 'rgba(255,255,255,0.8)', fontSize: 18 }}>{t('login.brandSubtitle')}</Typography>
        </Box>

        {/* Right Register Panel */}
        <Box sx={{ flexGrow: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: '#fff', p: '80px' }}>
          <Box sx={{ width: 400, display: 'flex', flexDirection: 'column', gap: 3 }}>
            <Box>
              <Typography sx={{ fontSize: 28, fontWeight: 700, color: 'text.primary' }}>{t('register.title')}</Typography>
              <Typography sx={{ fontSize: 14, color: 'text.secondary', mt: 1 }}>{t('register.subtitle')}</Typography>
            </Box>

            {error && <Alert severity="error" sx={{ borderRadius: 2 }} data-testid="register-error">{error}</Alert>}

            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <TextField fullWidth size="small" placeholder={t('register.usernamePlaceholder')} value={username} onChange={(e) => setUsername(e.target.value)} data-testid="register-username"
                slotProps={{ input: { startAdornment: <InputAdornment position="start"><PersonIcon sx={{ fontSize: 20, color: 'text.secondary' }} /></InputAdornment>, sx: fieldSx } }} />
              <TextField fullWidth size="small" placeholder={t('register.displayNamePlaceholder')} value={displayName} onChange={(e) => setDisplayName(e.target.value)} data-testid="register-displayName"
                slotProps={{ input: { startAdornment: <InputAdornment position="start"><BadgeIcon sx={{ fontSize: 20, color: 'text.secondary' }} /></InputAdornment>, sx: fieldSx } }} />
              <TextField fullWidth size="small" type={showPassword ? 'text' : 'password'} placeholder={t('register.passwordPlaceholder')} value={password} onChange={(e) => setPassword(e.target.value)} data-testid="register-password"
                slotProps={{ input: { startAdornment: <InputAdornment position="start"><LockIcon sx={{ fontSize: 20, color: 'text.secondary' }} /></InputAdornment>,
                  endAdornment: <InputAdornment position="end"><IconButton size="small" onClick={() => setShowPassword(!showPassword)}>{showPassword ? <VisibilityIcon sx={{ fontSize: 20 }} /> : <VisibilityOffIcon sx={{ fontSize: 20 }} />}</IconButton></InputAdornment>, sx: fieldSx } }} />
              <TextField fullWidth size="small" type={showPassword ? 'text' : 'password'} placeholder={t('register.confirmPasswordPlaceholder')} value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} data-testid="register-confirmPassword"
                slotProps={{ input: { startAdornment: <InputAdornment position="start"><LockIcon sx={{ fontSize: 20, color: 'text.secondary' }} /></InputAdornment>, sx: fieldSx } }} />
            </Box>

            <Button variant="contained" fullWidth onClick={handleRegister} disabled={loading} data-testid="register-submit"
              sx={{ height: 48, borderRadius: '8px', fontSize: 16, fontWeight: 600, textTransform: 'none' }}>
              {loading ? t('register.submitting') : t('register.submitBtn')}
            </Button>

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 0.5 }}>
              <Typography sx={{ fontSize: 14, color: 'text.secondary' }}>{t('register.hasAccount')}</Typography>
              <Link component="button" underline="none" onClick={() => navigate('/login')} sx={{ fontSize: 14, fontWeight: 600, color: 'primary.main' }}>{t('register.login')}</Link>
            </Box>
          </Box>
        </Box>
      </Box>
    </ThemeProvider>
  );
}
