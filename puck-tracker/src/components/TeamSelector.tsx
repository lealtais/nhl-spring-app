import { NhlTeam } from '../types';

interface TeamSelectorProps {
  teams: NhlTeam[];
  selectedTeam: NhlTeam | null;
  onTeamSelect: (team: NhlTeam) => void;
  accentColor: string;
}

export default function TeamSelector({ teams, selectedTeam, onTeamSelect, accentColor }: TeamSelectorProps) {
  return (
    <div className="team-selector">
      <div className="selector-icon">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke={accentColor} strokeWidth="2">
          <circle cx="12" cy="12" r="10" />
          <path d="M12 2 L12 22 M2 12 L22 12" />
        </svg>
      </div>
      <select
        id="team-select"
        value={selectedTeam?.abbrev ?? ''}
        onChange={(e) => {
          const team = teams.find((t) => t.abbrev === e.target.value);
          if (team) onTeamSelect(team);
        }}
        style={{ borderColor: accentColor }}
      >
        {teams.map((team) => (
          <option key={team.abbrev} value={team.abbrev}>
            {team.name}
          </option>
        ))}
      </select>
      <label htmlFor="team-select" style={{ color: accentColor }}>
        Select NHL Team
      </label>
    </div>
  );
}
