"use client";

import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export default function Header({ user }: { user: any }) {
  return (
    <header className="h-16 border-b bg-background flex items-center justify-between px-6">
      <h1 className="text-lg font-semibold">Dashboard</h1>

      <DropdownMenu>
        <DropdownMenuTrigger>
          <div className="flex items-center gap-2 cursor-pointer">
            <Avatar className="h-8 w-8">
              <AvatarFallback>{user?.email?.[0]?.toUpperCase() || "U"}</AvatarFallback>
            </Avatar>
            <span className="text-sm text-muted-foreground">{user.email}</span>
          </div>
        </DropdownMenuTrigger>

        <DropdownMenuContent align="end">
          <DropdownMenuItem>Perfil</DropdownMenuItem>
          <DropdownMenuItem>Configurações</DropdownMenuItem>
          <DropdownMenuItem
            className="text-red-600"
            onClick={() => (window.location.href = "/logout")}
          >
            Sair
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </header>
  );
}
