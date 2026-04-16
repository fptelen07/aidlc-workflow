import { useEffect, useState, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Chip from '@mui/material/Chip';
import Pagination from '@mui/material/Pagination';
import SearchIcon from '@mui/icons-material/Search';
import { userService } from '../../services/userService';

export default function AdminUsers() {
  const { t } = useTranslation();
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [users, setUsers] = useState<any[]>([]);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [keyword, setKeyword] = useState('');
  const size = 10;

  const fetchUsers = useCallback(async () => {
    try {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const res: any = await userService.listUsers({ page, size, keyword: keyword || undefined });
      const data = res?.data ?? res;
      setUsers(data?.records ?? data?.list ?? []);
      setTotal(data?.total ?? 0);
    } catch { setUsers([]); }
  }, [page, keyword]);

  useEffect(() => { fetchUsers(); }, [fetchUsers]);

  return (
    <Box sx={{ p: '32px', display: 'flex', flexDirection: 'column', gap: 3 }}>
      <Typography sx={{ fontSize: 24, fontWeight: 700 }}>{t('adminUsers.title')}</Typography>

      <TextField size="small" placeholder={t('adminUsers.searchPlaceholder')} value={keyword} onChange={(e) => { setKeyword(e.target.value); setPage(1); }}
        slotProps={{ input: { startAdornment: <SearchIcon sx={{ fontSize: 18, color: 'text.secondary', mr: 1 }} /> } }} sx={{ width: 300 }} data-testid="user-search" />

      <Paper elevation={0} sx={{ borderRadius: 3, border: '1px solid #F1F5F9' }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: '#F8FAFC' }}>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>ID</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminUsers.username')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminUsers.displayName')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminUsers.role')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminUsers.points')}</TableCell>
                <TableCell sx={{ fontWeight: 600, fontSize: 12, color: 'text.secondary' }}>{t('adminUsers.createdAt')}</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((u) => (
                <TableRow key={u.id}>
                  <TableCell sx={{ fontSize: 13 }}>{u.id}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{u.username}</TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{u.displayName}</TableCell>
                  <TableCell>
                    <Chip label={u.role === 'admin' ? t('adminUsers.admin') : t('adminUsers.employee')} size="small"
                      sx={{ fontSize: 11, color: u.role === 'admin' ? '#7C3AED' : '#2563EB', bgcolor: u.role === 'admin' ? '#EDE9FE' : '#EFF6FF' }} />
                  </TableCell>
                  <TableCell sx={{ fontSize: 13 }}>{u.points?.toLocaleString() ?? 0}</TableCell>
                  <TableCell sx={{ fontSize: 13, color: 'text.secondary' }}>{u.createdAt}</TableCell>
                </TableRow>
              ))}
              {users.length === 0 && <TableRow><TableCell colSpan={6} sx={{ textAlign: 'center', py: 4, color: 'text.secondary' }}>{t('common.noData')}</TableCell></TableRow>}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>

      {total > size && <Box sx={{ display: 'flex', justifyContent: 'center' }}><Pagination count={Math.ceil(total / size)} page={page} onChange={(_, p) => setPage(p)} data-testid="user-pagination" /></Box>}
    </Box>
  );
}
