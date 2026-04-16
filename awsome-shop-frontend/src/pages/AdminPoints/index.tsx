import { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Chip from '@mui/material/Chip';
import Switch from '@mui/material/Switch';
import Pagination from '@mui/material/Pagination';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import TollIcon from '@mui/icons-material/Toll';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import AccountBalanceIcon from '@mui/icons-material/AccountBalance';
import { pointsService } from '../../services/pointsService';

export default function AdminPoints() {
  const { t } = useTranslation();
  const [tab, setTab] = useState(0);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [stats, setStats] = useState<any>({});
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [history, setHistory] = useState<any[]>([]);
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [rules, setRules] = useState<any[]>([]);
  const [historyPage, setHistoryPage] = useState(1);
  const [historyTotal, setHistoryTotal] = useState(0);
  const [grantOpen, setGrantOpen] = useState(false);
  const [grantForm, setGrantForm] = useState({ userId: '', amount: '', reason: '' });
  const [batchOpen, setBatchOpen] = useState(false);
  const [batchForm, setBatchForm] = useState({ userIds: '', amount: '', reason: '' });
  const [ruleOpen, setRuleOpen] = useState(false);
  const [ruleForm, setRuleForm] = useState({ name: '', type: '', amount: '', description: '' });
  const size = 10;

  useEffect(() => {
    pointsService.getStatistics()
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      .then((res: any) => setStats(res?.data ?? res ?? {}))
      .catch(() => {});
  }, []);

  const fetchHistory = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await pointsService.getAllHistory({ page: historyPage, size });
      const data = res?.data ?? res;
      setHistory(data?.records ?? data?.list ?? []);
      setHistoryTotal(data?.total ?? 0);
    } catch { setHistory([]); }
  }, [historyPage]);

  const fetchRules = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await pointsService.listRules();
      setRules(res?.data ?? res ?? []);
    } catch { setRules([]); }
  }, []);

  useEffect(() => { if (tab === 1) fetchHistory(); }, [tab, fetchHistory]);
  useEffect(() => { if (tab === 2) fetchRules(); }, [tab, fetchRules]);

  const handleGrant = async () => {
    try {
      await pointsService.grant({ userId: Number(grantForm.userId), amount: Number(grantForm.amount), reason: grantForm.reason });
      setGrantOpen(false); setGrantForm({ userId: '', amount: '', reason: '' });
      if (tab === 1) fetchHistory();
    } catch { /* ignore */ }
  };

  const handleBatchGrant = async () => {
    try {
      const userIds = batchForm.userIds.split(',').map((s) => Number(s.trim())).filter(Boolean);
      await pointsService.batchGrant({ userIds, amount: Number(batchForm.amount), reason: batchForm.reason });
      setBatchOpen(false); setBatchForm({ userIds: '', amount: '', reason: '' });
      if (tab === 1) fetchHistory();
    } catch { /* ignore */ }
  };

  const handleCreateRule = async () => {
    try {
      await pointsService.createRule(ruleForm);
      setRuleOpen(false); setRuleForm({ name: '', type: '', amount: '', description: '' });
      fetchRules();
    } catch { /* ignore */ }
  };

  const handleToggleRule = async (id: number) => {
    try { await pointsService.toggleRule(id); fetchRules(); } catch { /* ignore */ }
  };

  const STAT_CARDS = [
    { key: 'monthlyGrant', icon: TrendingUpIcon, color: '#16A34A', bg: '#DCFCE7', value: stats.monthlyGrant ?? stats.monthlyGrantAmount ?? 0 },
    { key: 'monthlyDeduct', icon: TrendingDownIcon, color: '#DC2626', bg: '#FEE2E2', value: stats.monthlyDeduct ?? stats.monthlyDeductAmount ?? 0 },
    { key: 'circulation', icon: AccountBalanceIcon, color: '#7C3AED', bg: '#EDE9FE', value: stats.circulation ?? stats.totalCirculation ?? 0 },
  ];

  return (
    <Box sx={{ p: '32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('adminPoints.title')}</Typography>
        <Box sx={{ display: 'flex', gap: 1 }}>
          <Button variant="contained" onClick={() => setGrantOpen(true)} data-testid="grant-btn" sx={{ textTransform: 'none' }}>{t('adminPoints.grant')}</Button>
          <Button variant="outlined" onClick={() => setBatchOpen(true)} data-testid="batch-grant-btn" sx={{ textTransform: 'none' }}>{t('adminPoints.batchGrant')}</Button>
        </Box>
      </Box>

      {/* Stats Cards */}
      <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 2 }}>
        {STAT_CARDS.map((s) => {
          const Icon = s.icon;
          return (
            <Paper key={s.key} elevation={0} sx={{ p: 2.5, borderRadius: 3, border: '1px solid #F1F5F9', display: 'flex', alignItems: 'center', gap: 2 }}>
              <Box sx={{ width: 44, height: 44, borderRadius: 2, bgcolor: s.bg, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Icon sx={{ color: s.color }} />
              </Box>
              <Box>
                <Typography sx={{ fontSize: 12, color: 'text.secondary' }}>{t(`adminPoints.${s.key}`)}</Typography>
                <Typography sx={{ fontSize: 22, fontWeight: 700 }}>{Number(s.value).toLocaleString()}</Typography>
              </Box>
            </Paper>
          );
        })}
      </Box>

      <Tabs value={tab} onChange={(_, v) => setTab(v)}>
        <Tab label={t('adminPoints.overview')} />
        <Tab label={t('adminPoints.historyTab')} />
        <Tab label={t('adminPoints.rulesTab')} />
      </Tabs>

      {tab === 0 && (
        <Paper elevation={0} sx={{ p: 4, borderRadius: 3, border: '1px solid #F1F5F9', textAlign: 'center' }}>
          <TollIcon sx={{ fontSize: 48, color: '#D97706', mb: 2 }} />
          <Typography sx={{ fontSize: 16, color: 'text.secondary' }}>{t('adminPoints.overviewHint')}</Typography>
        </Paper>
      )}

      {tab === 1 && (
        <>
          <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.user')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.type')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.amount')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.reason')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.time')}</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {history.map((h, idx) => (
                    <TableRow key={h.id ?? idx}>
                      <TableCell sx={{ fontSize: 13 }}>{h.userName || h.userId}</TableCell>
                      <TableCell><Chip label={h.type} size="small" sx={{ fontSize: 11 }} /></TableCell>
                      <TableCell sx={{ fontSize: 13, fontWeight: 600, color: h.type === 'grant' ? '#16A34A' : '#DC2626' }}>
                        {h.type === 'grant' ? '+' : '-'}{Math.abs(h.amount)?.toLocaleString()}
                      </TableCell>
                      <TableCell sx={{ fontSize: 13 }}>{h.reason}</TableCell>
                      <TableCell sx={{ fontSize: 13, color: 'text.secondary' }}>{h.createdAt}</TableCell>
                    </TableRow>
                  ))}
                  {history.length === 0 && <TableRow><TableCell colSpan={5} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
          {historyTotal > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}><Pagination count={Math.ceil(historyTotal / size)} page={historyPage} onChange={(_, p) => setHistoryPage(p)} /></Box>}
        </>
      )}

      {tab === 2 && (
        <>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Button variant="contained" onClick={() => setRuleOpen(true)} data-testid="add-rule-btn" sx={{ textTransform: 'none' }}>{t('adminPoints.addRule')}</Button>
          </Box>
          <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.ruleName')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.ruleType')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.ruleAmount')}</TableCell>
                    <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminPoints.ruleEnabled')}</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {rules.map((r) => (
                    <TableRow key={r.id}>
                      <TableCell sx={{ fontSize: 13 }}>{r.name}</TableCell>
                      <TableCell sx={{ fontSize: 13 }}>{r.type}</TableCell>
                      <TableCell sx={{ fontSize: 13 }}>{r.amount}</TableCell>
                      <TableCell><Switch checked={r.enabled} onChange={() => handleToggleRule(r.id)} size="small" data-testid={`toggle-rule-${r.id}`} /></TableCell>
                    </TableRow>
                  ))}
                  {rules.length === 0 && <TableRow><TableCell colSpan={4} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </>
      )}

      {/* Grant Dialog */}
      <Dialog open={grantOpen} onClose={() => setGrantOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{t('adminPoints.grant')}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '16px !important' }}>
          <TextField label={t('adminPoints.userId')} value={grantForm.userId} onChange={(e) => setGrantForm({ ...grantForm, userId: e.target.value })} fullWidth size="small" data-testid="grant-userId" />
          <TextField label={t('adminPoints.amount')} type="number" value={grantForm.amount} onChange={(e) => setGrantForm({ ...grantForm, amount: e.target.value })} fullWidth size="small" data-testid="grant-amount" />
          <TextField label={t('adminPoints.reason')} value={grantForm.reason} onChange={(e) => setGrantForm({ ...grantForm, reason: e.target.value })} fullWidth size="small" data-testid="grant-reason" />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setGrantOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" onClick={handleGrant} data-testid="grant-submit">{t('common.confirm')}</Button>
        </DialogActions>
      </Dialog>

      {/* Batch Grant Dialog */}
      <Dialog open={batchOpen} onClose={() => setBatchOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{t('adminPoints.batchGrant')}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '16px !important' }}>
          <TextField label={t('adminPoints.userIds')} value={batchForm.userIds} onChange={(e) => setBatchForm({ ...batchForm, userIds: e.target.value })} fullWidth size="small" helperText={t('adminPoints.userIdsHint')} data-testid="batch-userIds" />
          <TextField label={t('adminPoints.amount')} type="number" value={batchForm.amount} onChange={(e) => setBatchForm({ ...batchForm, amount: e.target.value })} fullWidth size="small" data-testid="batch-amount" />
          <TextField label={t('adminPoints.reason')} value={batchForm.reason} onChange={(e) => setBatchForm({ ...batchForm, reason: e.target.value })} fullWidth size="small" data-testid="batch-reason" />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setBatchOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" onClick={handleBatchGrant} data-testid="batch-submit">{t('common.confirm')}</Button>
        </DialogActions>
      </Dialog>

      {/* Rule Dialog */}
      <Dialog open={ruleOpen} onClose={() => setRuleOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{t('adminPoints.addRule')}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '16px !important' }}>
          <TextField label={t('adminPoints.ruleName')} value={ruleForm.name} onChange={(e) => setRuleForm({ ...ruleForm, name: e.target.value })} fullWidth size="small" data-testid="rule-name" />
          <TextField label={t('adminPoints.ruleType')} value={ruleForm.type} onChange={(e) => setRuleForm({ ...ruleForm, type: e.target.value })} fullWidth size="small" data-testid="rule-type" />
          <TextField label={t('adminPoints.ruleAmount')} type="number" value={ruleForm.amount} onChange={(e) => setRuleForm({ ...ruleForm, amount: e.target.value })} fullWidth size="small" data-testid="rule-amount" />
          <TextField label={t('adminPoints.ruleDescription')} value={ruleForm.description} onChange={(e) => setRuleForm({ ...ruleForm, description: e.target.value })} fullWidth size="small" multiline rows={2} data-testid="rule-desc" />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setRuleOpen(false)}>{t('common.cancel')}</Button>
          <Button variant="contained" onClick={handleCreateRule} data-testid="rule-submit">{t('common.save')}</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
