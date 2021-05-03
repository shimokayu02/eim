import * as Enums from '@/enums'

export function statusType(v) {
  return v ? Enums.EmployeeStatusType[v] : '-'
}