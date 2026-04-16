import { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Chip from '@mui/material/Chip';
import Pagination from '@mui/material/Pagination';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import TollIcon from '@mui/icons-material/Toll';
import { pointsService } from '../../services/pointsService';

const TYPE_COLORS: Record<string, { color: string; bg: string }> = {
  grant: { color: '#166534', bg: '#DCFCE7' },
  deduct: { color: '#991B1B', bg: '#FEE2E2' },
  expire: { color: '#92400E', bg: '#FEF3C7' },
};

export default function MyPoints() {
  const { t } = useTranslation();
  const [balance, setBalance] = useState(0);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [history, setHistory] = useState<any[]>([]);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [type, setType] = useState('');
  const size = 10;

  useEffect(() => {
    pointsService.getBalance()
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => { const d = res?.data ?? res; setBalance(d?.balance ?? d ?? 0); })
      .catch(() => {});
  }, []);

  const fetchHistory = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await pointsService.getMyHistory({ page, size, type: type || undefined });
      const data = res?.data ?? res;
      setHistory(data?.records ?? data?.list ?? []);
      setTotal(data?.total ?? 0);
    } catch { setHistory([]); }
  }, [page, type]);

  useEffect(() => { fetchHistory(); }, [fetchHistory]);

  return (
    <Box sx={{ p: '24px 32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('myPoints.title')}</Typography>

      <Paper elevation={0} sx={{ p: 3, borderRadius: 3, border: '1px solid #F1F5F9', display: 'flex', alignItems: 'center', gap: 2 }}>
        <TollIcon sx={{ fontSize: 40, color: '#D97706' }} />
        <Box>
          <Typography sx={{ fontSize: 14, color: 'text.secondary' }}>{t('myPoints.balance')}</Typography>
          <Typography sx={{ fontSize: 32, fontWeight: 700, color: '#D97706' }} data-testid="points-balance">{balance.toLocaleString()}</Typography>
        </Box>
      </Paper>

      <ToggleButtonGroup value={type} exclusive onChange={(_, v) => { setType(v ?? ''); setPage(1); }} size="small" data-testid="points-type-filter">
        <ToggleButton value="">{t('myPoints.all')}</ToggleButton>
        <ToggleButton value="grant">{t('myPoints.grant')}</ToggleButton>
        <ToggleButton value="deduct">{t('myPoints.deduct')}</ToggleButton>
        <ToggleButton value="expire">{t('myPoints.expire')}</ToggleButton>
      </ToggleButtonGroup>

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myPoints.type')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myPoints.amount')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myPoints.reason')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('myPoints.time')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {history.map((item, idx) => {
                const tc = TYPE_COLORS[item.type] ?? TYPE_COLORS.grant;
                return (
                  <TableRow key={item.id ?? idx}>
                    <TableCell><Chip label={t(`myPoints.${item.type}`)} size="small" sx={{ color: tc.color, bgcolor: tc.bg, fontSize: 11 }} /></TableCell>
                    <TableCell sx={{ fontSize: 13, fontWeight: 600, color: item.type === 'grant' ? '#16A34A' : '#DC2626' }}>
                      {item.type === 'grant' ? '+' : '-'}{Math.abs(item.amount)?.toLocaleString()}
                    </TableCell>
                    <TableCell sx={{ fontSize: 13 }}>{item.reason}</TableCell>
                    <TableCell sx={{ fontSize: 13, color: 'text.secondary' }}>{item.createdAt}</TableCell>
                  </TableRow>
                );
              })}
              {history.length === 0 && (
                <TableRow><TableCell colSpan={4} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {total > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}>
        <Pagination count={Math.ceil(total / size)} page={page} onChange={(_, p) => setPage(p)} data-testid="points-pagination" />
      </Box>}
    </Box>
  );
}
